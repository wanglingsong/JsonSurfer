/*
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

package org.leo.json.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class JsonPath {

    public static class Builder {

        private JsonPath jsonPath;

        public static Builder start() {
            Builder builder = new Builder();
            JsonPath newPath = new JsonPath();
            newPath.operators.push(Root.instance());
            builder.jsonPath = newPath;
            return builder;
        }

        public Builder child(String key) {
            jsonPath.operators.push(new ChildNode(key));
            return this;
        }

        public Builder children(String... children) {
            jsonPath.operators.push(new ChildrenNode(new HashSet<String>(Arrays.asList(children))));
            return this;
        }

        public Builder anyChild() {
            jsonPath.operators.push(AnyChild.instance());
            return this;
        }


        public Builder index(int index) {
            jsonPath.operators.push(new ArrayIndex(index));
            return this;
        }

        public Builder indexes(Integer... indexes) {
            jsonPath.operators.push(new ArrayIndexes(new HashSet<Integer>(Arrays.asList(indexes))));
            return this;
        }

        public Builder anyIndex() {
            jsonPath.operators.push(AnyIndex.instance());
            return this;
        }

        public Builder scan() {
            jsonPath.definite = false;
            // TODO Singleton
            jsonPath.operators.push(new DeepScan());
            return this;
        }

        public Builder any() {
            // TODO Singleton
            jsonPath.operators.push(new Wildcard());
            return this;
        }

        public JsonPath build() {
            return this.jsonPath;
        }

    }

    private boolean definite = true;

    protected LinkedList<PathOperator> operators = new LinkedList<PathOperator>();

    public boolean match(JsonPath jsonPath) {
        Iterator<PathOperator> iterator1 = operators.iterator();
        Iterator<PathOperator> iterator2 = jsonPath.operators.iterator();
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            PathOperator o1 = iterator1.next();
            PathOperator o2 = iterator2.next();
            if (o1.getType() == PathOperator.Type.DEEP_SCAN) {
                PathOperator prevScan = iterator1.next();
                while (!prevScan.match(o2) && iterator2.hasNext()) {
                    o2 = iterator2.next();
                }
                continue;
            }
            if (!o1.match(o2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }

    public PathOperator peek() {
        return operators.peek();
    }

    public int pathDepth() {
        return this.operators.size();
    }

    public boolean isDefinite() {
        return definite;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<PathOperator> dItr = operators.descendingIterator();
        while (dItr.hasNext()) {
            sb.append(dItr.next());
        }
        return sb.toString();
    }

}
