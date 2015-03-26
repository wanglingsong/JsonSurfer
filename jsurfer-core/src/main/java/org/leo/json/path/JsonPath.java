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

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class JsonPath {

    //TODO recycling node for saving gc

    protected LinkedList<PathOperator> path = new LinkedList<PathOperator>();

    private boolean definite = true;

    public boolean match(JsonPath jsonPath) {
        Iterator<PathOperator> iterator1 = path.iterator();
        Iterator<PathOperator> iterator2 = jsonPath.path.iterator();
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

    public static JsonPath start() {
        JsonPath newPath = new JsonPath();
        newPath.path.push(Root.instance());
        return newPath;
    }

    public JsonPath child(String key) {
        path.push(new ChildNode(key));
        return this;
    }

    public JsonPath children(String... children) {
        path.push(new ChildrenNode(Sets.newHashSet(children)));
        return this;
    }

    public JsonPath childWildcard() {
        path.push(ChildWildcard.instance());
        return this;
    }


    public JsonPath indexes(Integer... indexes) {
        path.push(new ArrayIndexes(Sets.newHashSet(indexes)));
        return this;
    }

    public JsonPath arrayWildcard() {
        path.push(ArrayWildcard.instance());
        return this;
    }

    public JsonPath index(int index) {
        path.push(new ArrayIndex(index));
        return this;
    }

    public JsonPath scan() {
        this.definite = false;
        // TODO Singleton
        path.push(new DeepScan());
        return this;
    }

    public JsonPath wildcard() {
        // TODO Singleton
        path.push(new Wildcard());
        return this;
    }

    public PathOperator peek() {
        return path.peek();
    }

    public int pathDepth() {
        return this.path.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<PathOperator> dItr = path.descendingIterator();
        while (dItr.hasNext()) {
            sb.append(dItr.next());
        }
        return sb.toString();
    }

    public boolean isDefinite() {
        return definite;
    }

}
