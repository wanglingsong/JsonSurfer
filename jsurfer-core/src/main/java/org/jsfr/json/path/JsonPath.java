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

public class JsonPath implements Iterable<PathOperator> {

    private class JsonPathIterator implements Iterator<PathOperator> {

        private JsonPathNode current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public PathOperator next() {
            PathOperator op = current.operator();
            current = current.next();
            return op;
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

    protected JsonPathNode head;
    protected JsonPathNode tail;
    protected int size;

    protected JsonPath() {
        head = new JsonPathNode(null, Root.instance());
        tail = head;
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
        JsonPathNode pointer1 = this.tail;
        JsonPathNode pointer2 = jsonPath.tail;
        if (!pointer1.operator().match(pointer2.operator())) {
            return false;
        }
        while (pointer1.hasPrevious()) {
            if (!pointer2.hasPrevious()) {
                return false;
            }
            PathOperator o1 = (pointer1 = pointer1.previous()).operator();
            PathOperator o2 = (pointer2 = pointer2.previous()).operator();
            if (o1.getType() == PathOperator.Type.DEEP_SCAN) {
                PathOperator prevScan = (pointer1 = pointer1.previous()).operator();
                while (!prevScan.match(o2) && pointer2.hasPrevious()) {
                    o2 = (pointer2 = pointer2.previous()).operator();
                }
            } else {
                if (!o1.match(o2)) {
                    return false;
                }
            }
        }
        return !pointer2.hasPrevious();
    }

    public PathOperator peek() {
        return tail.operator();
    }

    protected void push(PathOperator operator) {
        this.tail = this.tail.createNext(operator);
        this.size++;
    }

    protected PathOperator pop() {
        JsonPathNode top = tail;
        tail = top.previous();
        tail.resetNext();
        PathOperator topOperator = top.operator();
        top.reset();
        this.size--;
        return topOperator;
    }

    public int pathDepth() {
        return this.size;
    }

    public void clear() {
        while (tail.hasPrevious()) {
            pop();
        }
        tail.reset();
        head = null;
        tail = null;
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
