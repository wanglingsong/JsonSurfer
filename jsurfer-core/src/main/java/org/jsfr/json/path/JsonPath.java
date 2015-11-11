/*
 * The MIT License
 *
 * Copyright (c) 2015 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jsfr.json.path;

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

        public Builder child(String key) {
            jsonPath.push(new ChildNode(key));
            return this;
        }

        public Builder children(String... children) {
            jsonPath.push(new ChildrenNode(new HashSet<String>(Arrays.asList(children))));
            return this;
        }

        public Builder anyChild() {
            jsonPath.push(AnyChild.instance());
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
            jsonPath.push(AnyIndex.instance());
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

        public JsonPath build() {
            if (jsonPath.peek().getType() == PathOperator.Type.DEEP_SCAN) {
                throw new IllegalStateException("deep-scan shouldn't be the last operator.");
            }
            if (!jsonPath.definite) {
                // calculate minimum depth
                for (PathOperator operator : jsonPath) {
                    if (!(operator.getType() == PathOperator.Type.DEEP_SCAN)) {
                        jsonPath.minimumDepth++;
                    }
                }
            }
            return this.jsonPath;
        }

    }

    private boolean definite = true;
    private int minimumDepth = 0;

    protected PathOperator[] operators;
    protected int size;

    protected JsonPath() {
        operators = new PathOperator[JSON_PATH_INITIAL_CAPACITY];
        operators[0] = Root.instance();
        size = 1;
    }

    public Object resolve(Object document, DocumentResolver resolver) {
        if (!this.isDefinite()) {
            throw new IllegalArgumentException("Indefinite JsonPath is not supported.");
        }
        Object current = document;
        for (PathOperator pathOperator : this) {
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
        while (pointer1 >= 0) {
            if (!(pointer2 >= 0)) {
                return false;
            }
            PathOperator o1 = this.get(pointer1--);
            PathOperator o2 = jsonPath.get(pointer2--);
            if (o1.getType() == PathOperator.Type.DEEP_SCAN) {
                PathOperator prevScan = this.get(pointer1--);
                while (!prevScan.match(o2) && pointer2 >= 0) {
                    o2 = jsonPath.get(pointer2--);
                }
            } else {
                if (!o1.match(o2)) {
                    return false;
                }
            }
        }
        return !(pointer2 >= 0);
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

    protected PathOperator pop() {
        return operators[--size];
    }

    public int pathDepth() {
        return this.size;
    }

    public void clear() {
        operators = null;
    }

    public int minimumPathDepth() {
        if (definite) {
            return this.pathDepth();
        } else {
            return minimumDepth;
        }
    }

    public boolean isDefinite() {
        return definite;
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
