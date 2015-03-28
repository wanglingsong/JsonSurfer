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

package org.leo.json.parse;

import org.leo.json.path.*;

import java.lang.ref.SoftReference;
import java.util.Stack;

/**
 * Created by Leo on 2015/3/27.
 */
class JsonPosition extends JsonPath {

    //TODO recycling node for saving gc

    private SoftReference<Stack<ChildNode>> childNodeCache = new SoftReference<Stack<ChildNode>>(new Stack<ChildNode>());
    private SoftReference<Stack<ArrayIndex>> arrayNodeCache = new SoftReference<Stack<ArrayIndex>>(new Stack<ArrayIndex>());

    static JsonPosition start() {
        JsonPosition newPath = new JsonPosition();
        newPath.operators.push(Root.instance());
        return newPath;
    }

    void stepOut() {
        PathOperator node = operators.pop();
        if (node.getType() == PathOperator.Type.OBJECT) {
            Stack<ChildNode> stack = childNodeCache.get();
            if (stack != null) {
                stack.push((ChildNode) node);
            } else {
                createChildNodeCache();
            }
        } else if (node.getType() == PathOperator.Type.ARRAY) {
            Stack<ArrayIndex> stack = arrayNodeCache.get();
            if (stack != null) {
                stack.push((ArrayIndex) node);
            } else {
                createArrayNodeCache();
            }
        }
    }

    void stepInArray() {
        Stack<ArrayIndex> stack = arrayNodeCache.get();
        ArrayIndex node = null;
        if (stack != null && !stack.isEmpty()) {
            node = stack.pop();
            node.reset();
        }
        if (node == null) {
            node = new ArrayIndex();
        }
        operators.push(node);
    }

    void stepInObject(String key) {
        Stack<ChildNode> stack = childNodeCache.get();
        ChildNode node = null;
        if (stack != null && !stack.isEmpty()) {
            node = stack.pop();
            node.setKey(key);
        }
        if (node == null) {
            node = new ChildNode(key);
        }
        operators.push(node);
    }

    void clear() {
        operators.clear();
        operators = null;
    }

    private void createChildNodeCache() {
        childNodeCache = new SoftReference<Stack<ChildNode>>(new Stack<ChildNode>());
    }

    private void createArrayNodeCache() {
        arrayNodeCache = new SoftReference<Stack<ArrayIndex>>(new Stack<ArrayIndex>());
    }

}
