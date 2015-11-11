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

package org.jsfr.json;

import org.jsfr.json.provider.JsonProvider;

public class JsonDomBuilder implements JsonSaxHandler {

    private static final int ROOT = 0;
    private static final int IN_OBJECT = 1;
    private static final int IN_ARRAY = 2;

    private static class Node {

        private int scope;
        private Object value;

    }

    private JsonProvider provider;
    private String propertyName;

    private Node[] stack = new Node[32];

    private int stackSize = 0;

    {
        this.push(ROOT, null);
    }

    private void push(int newTop, Object topValue) {
        if (stackSize == stack.length) {
            Node[] newStack = new Node[stackSize * 2];
            System.arraycopy(stack, 0, newStack, 0, stackSize);
            stack = newStack;
        }

        Node next = stack[stackSize];
        if (next == null) {
            next = new Node();
            stack[stackSize] = next;
        }
        next.value = topValue;
        next.scope = newTop;
        stackSize++;
    }

    private Node peekNode() {
        return stack[stackSize - 1];
    }

    private int peek() {
        return peekNode().scope;
    }

    protected Object peekValue() {
        return peekNode().value;
    }

    private void replaceTop(Object value) {
        stack[stackSize - 1].value = value;
    }

    private void pop() {
        stackSize--;
    }

    public void setProvider(JsonProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean startJSON() {
        return true;
    }

    @Override
    public boolean endJSON() {
        return true;
    }

    @Override
    public boolean startObject() {
        Object newObject = provider.createObject();
        Node top = peekNode();
        switch (top.scope) {
            case ROOT:
                replaceTop(newObject);
                break;
            case IN_OBJECT:
                provider.put(top.value, propertyName, newObject);
                propertyName = null;
                break;
            case IN_ARRAY:
                provider.add(top.value, newObject);
                break;
            default:
                throw new IllegalStateException();
        }
        push(IN_OBJECT, newObject);
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        switch (peek()) {
            case IN_OBJECT:
                propertyName = key;
                break;
            case IN_ARRAY:
                throw new IllegalStateException();
        }
        return true;
    }

    @Override
    public boolean endObject() {
        switch (peek()) {
            case IN_OBJECT:
                pop();
                break;
            case IN_ARRAY:
                throw new IllegalStateException();
        }
        return false;
    }

    @Override
    public boolean startArray() {
        Object newArray = provider.createArray();
        Node top = peekNode();
        switch (top.scope) {
            case ROOT:
                replaceTop(newArray);
                break;
            case IN_OBJECT:
                provider.put(top.value, propertyName, newArray);
                propertyName = null;
                break;
            case IN_ARRAY:
                provider.add(top.value, newArray);
                break;
            default:
                throw new IllegalStateException();
        }
        push(IN_ARRAY, newArray);
        return true;
    }

    @Override
    public boolean endArray() {
        pop();
        return true;
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        Object value = primitiveHolder.getValue();
        Node top = peekNode();
        switch (top.scope) {
            case ROOT:
                replaceTop(value);
                break;
            case IN_OBJECT:
                provider.put(top.value, propertyName, value);
                propertyName = null;
                break;
            case IN_ARRAY:
                provider.add(top.value, value);
                break;
            default:
                throw new IllegalStateException();
        }
        return true;
    }

    public boolean isInRoot() {
        return peek() == ROOT;
    }


    public void clear() {
        propertyName = null;
        provider = null;
        stack = null;
    }

}
