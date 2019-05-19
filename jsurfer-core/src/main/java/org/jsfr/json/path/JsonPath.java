/*
 * MIT License
 *
 * Copyright (c) 2019 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jsfr.json.path;

import org.jsfr.json.filter.JsonPathFilter;
import org.jsfr.json.resolver.DocumentResolver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class JsonPath implements Iterable<PathOperator> {

    private static final int JSON_PATH_INITIAL_CAPACITY = 20;

    private class JsonPathIterator implements Iterator<PathOperator> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public PathOperator next() {
            if (current >= size) {
                throw new NoSuchElementException();
            } else {
                return operators[current++];
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("unsupported");
        }

    }

    @Override
    public Iterator<PathOperator> iterator() {
        return new JsonPathIterator();
    }

    public static class Builder {

        private JsonPath jsonPath;

        public static Builder start() {
            Builder builder = new Builder();
            builder.jsonPath = new JsonPath();
            return builder;
        }

        public static Builder startFilterPath() {
            Builder builder = new Builder();
            builder.jsonPath = new JsonPath(true);
            return builder;
        }

        public Builder child(String key) {
            jsonPath.push(new ChildNode(key));
            return this;
        }

        public Builder children(String... children) {
            jsonPath.push(new ChildrenNode(new HashSet<String>(Arrays.asList(children))));
            return this;
        }

        public Builder anyChild() {
            jsonPath.push(Wildcard.SINGLETON);
            return this;
        }


        public Builder index(int index) {
            jsonPath.push(new ArrayIndex(index));
            return this;
        }

        public Builder indexes(Integer... indexes) {
            jsonPath.push(new ArrayIndexes(new HashSet<Integer>(Arrays.asList(indexes))));
            return this;
        }

        public Builder anyIndex() {
            jsonPath.push(Wildcard.SINGLETON);
            return this;
        }

        public Builder scan() {
            jsonPath.definite = false;
            if (!(jsonPath.peek().getType() == PathOperator.Type.DEEP_SCAN)) {
                jsonPath.push(DeepScan.SINGLETON);
            }
            return this;
        }

        public Builder any() {
            jsonPath.push(Wildcard.SINGLETON);
            return this;
        }

        public Builder slicing(Integer lower, Integer upper) {
            jsonPath.push(new ArraySlicing(lower, upper));
            return this;
        }

        public Builder arrayFilter(JsonPathFilter jsonPathFilter) {
            jsonPath.push(new ArrayFilter(jsonPathFilter));
            return this;
        }

        public JsonPath build() {
            if (jsonPath.peek().getType() == PathOperator.Type.DEEP_SCAN) {
                throw new IllegalStateException("deep-scan shouldn't be the last operator.");
            }
            return this.jsonPath;
        }

    }

    private boolean definite = true;

    protected PathOperator[] operators;
    protected int size;

    protected JsonPath() {
        this(false);
    }

    protected JsonPath(boolean filterPath) {
        operators = new PathOperator[JSON_PATH_INITIAL_CAPACITY];
        if (filterPath) {
            operators[0] = FilterRoot.instance();
        } else {
            operators[0] = Root.instance();
        }
        size = 1;
    }

    public Object resolve(Object document, DocumentResolver resolver) {
        if (!this.isDefinite()) {
            throw new IllegalArgumentException("Indefinite JsonPath is not supported.");
        }
        Object current = document;
        for (PathOperator pathOperator : this) {
            if (current == null) {
                return null;
            }
            current = pathOperator.resolve(current, resolver);
        }
        return current;
    }

    public boolean match(JsonPath jsonPath) {
        int pointer1 = this.size - 1;
        int pointer2 = jsonPath.size - 1;
        if (!get(pointer1).match(jsonPath.get(pointer2))) {
            return false;
        }
        pointer1--;
        pointer2--;
        while (pointer1 >= 0) {
            if (!(pointer2 >= 0)) {
                return false;
            }
            PathOperator o1 = this.get(pointer1--);
            PathOperator o2 = jsonPath.get(pointer2--);
            if (!o1.match(o2)) {
                return false;
            }
        }
        return !(pointer2 >= 0);
    }

    public boolean matchWithDeepScan(JsonPath jsonPath) {
        int pointer1 = this.size - 1;
        int pointer2 = jsonPath.size - 1;
        if (!get(pointer1).match(jsonPath.get(pointer2))) {
            return false;
        }
        pointer1--;
        pointer2--;
        while (pointer1 >= 0) {
            if (!(pointer2 >= 0)) {
                return false;
            }
            PathOperator o1 = this.get(pointer1--);
            PathOperator o2 = jsonPath.get(pointer2--);
            if (o1.getType() == PathOperator.Type.DEEP_SCAN) {
                int blockHead = indexOfPreviousDeepScanOrRoot(this, pointer1);
                int blockSize = pointer1 - blockHead;
                int offset2 = pointer2 - blockSize + 2;
                while (offset2 > 0 && !matchPathBlock(this, blockHead + 1, jsonPath, offset2, blockSize)) {
                    offset2--;
                }
                pointer1 = blockHead;
                pointer2 = offset2 - 1;
            } else {
                if (!o1.match(o2)) {
                    return false;
                }
            }
        }
        return !(pointer2 >= 0);
    }

    private boolean matchPathBlock(JsonPath path1, int offset1, JsonPath path2, int offset2, int blockSize) {
        for (int i = 0; i < blockSize; i++) {
            if (!path1.get(offset1 + i).match(path2.get(offset2 + i))) {
                return false;
            }
        }
        return true;
    }

    private int indexOfPreviousDeepScanOrRoot(JsonPath path, int from) {
        int pointer = from - 1;
        while (pointer > 0) {
            if (path.get(pointer).getType() == PathOperator.Type.DEEP_SCAN) {
                return pointer;
            } else {
                pointer--;
            }
        }
        return pointer;
    }

    public boolean matchFilterPath(JsonPath jsonPath) {
        int pointer1 = this.size - 1;
        int pointer2 = jsonPath.size - 1;
        if (!get(pointer1).match(jsonPath.get(pointer2))) {
            return false;
        }
        pointer1--;
        pointer2--;
        while (pointer1 >= 0) {
            if (!(pointer2 >= 0)) {
                return false;
            }
            PathOperator o1 = this.get(pointer1--);
            PathOperator o2 = jsonPath.get(pointer2--);
            // TODO Allow deep scan in filter path?
            if (o1.getType() == PathOperator.Type.FILTER_ROOT) {
                return true;
            } else {
                if (!o1.match(o2)) {
                    return false;
                }
            }
        }
        return !(pointer2 >= 0);
    }

    public JsonPath derivePath(int depth) {
        JsonPath newPath = new JsonPath();
        newPath.size = depth;
        newPath.operators = this.operators;
        return newPath;
    }

    public PathOperator get(int i) {
        return operators[i];
    }

    public PathOperator peek() {
        return operators[size - 1];
    }

    protected void push(PathOperator operator) {
        ensureCapacity(size + 1);
        operators[size++] = operator;
    }

    private void ensureCapacity(int capacity) {
        if (operators.length < capacity) {
            PathOperator[] newOperators = new PathOperator[operators.length * 2];
            System.arraycopy(operators, 0, newOperators, 0, operators.length);
            operators = newOperators;
        }
    }

    protected void pop() {
        size--;
    }

    public int pathDepth() {
        return this.size;
    }

    public void clear() {
        operators = null;
    }

    public static int minimumPathDepth(JsonPath path) {
        if (path.definite) {
            return path.pathDepth();
        } else {
            int minimumDepth = 0;
            for (PathOperator operator : path) {
                if (!(operator.getType() == PathOperator.Type.DEEP_SCAN)) {
                    minimumDepth++;
                }
            }
            return minimumDepth;
        }
    }

    public boolean isDefinite() {
        return definite;
    }

    public boolean checkDefinite() {
        for (PathOperator operator : this) {
            if (operator.getType() == PathOperator.Type.DEEP_SCAN) {
                this.definite = false;
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PathOperator operator : this) {
            sb.append(operator);
        }
        return sb.toString();
    }

}
