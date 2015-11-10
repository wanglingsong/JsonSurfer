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

    private JsonProvider provider;
    private String propertyName;

    private int[] scopeStack = new int[32];
    private Object[] valueStack = new Object[32];
    private int stackSize = 0;

    {
        this.push(ROOT, null);
    }

    private void push(int newTop, Object topValue) {
        if (stackSize == scopeStack.length) {
            int[] newStack = new int[stackSize * 2];
            System.arraycopy(scopeStack, 0, newStack, 0, stackSize);
            scopeStack = newStack;
            Object[] newValueStack = new Object[stackSize * 2];
            System.arraycopy(valueStack, 0, newValueStack, 0, stackSize);
            valueStack = newValueStack;
        }

        scopeStack[stackSize] = newTop;
        valueStack[stackSize] = topValue;
        stackSize++;
    }

    private int peek() {
        return scopeStack[stackSize - 1];
    }

    protected Object peekValue() {
        return valueStack[stackSize - 1];
    }

    private void replaceTop(Object value) {
        valueStack[stackSize - 1] = value;
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
        switch (peek()) {
            case ROOT:
                replaceTop(newObject);
                break;
            case IN_OBJECT:
                provider.put(peekValue(), propertyName, newObject);
                propertyName = null;
                break;
            case IN_ARRAY:
                provider.add(peekValue(), newObject);
                break;
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
        switch (peek()) {
            case ROOT:
                replaceTop(newArray);
                break;
            case IN_OBJECT:
                provider.put(peekValue(), propertyName, newArray);
                propertyName = null;
                break;
            case IN_ARRAY:
                provider.add(peekValue(), newArray);
                break;
        }
        push(IN_ARRAY, newArray);
        return true;
    }

    @Override
    public boolean endArray() {
        pop();
        return true;
    }

    private void consumePrimitive(Object value) {
        switch (peek()) {
            case ROOT:
                replaceTop(value);
                break;
            case IN_OBJECT:
                provider.put(peekValue(), propertyName, value);
                propertyName = null;
                break;
            case IN_ARRAY:
                provider.add(peekValue(), value);
                break;
        }
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        consumePrimitive(primitiveHolder.getValue());
        return true;
    }

    public boolean isInRoot() {
        return peek() == ROOT;
    }


    public void clear() {
        propertyName = null;
        provider = null;
        scopeStack = null;
        valueStack = null;
    }

}
