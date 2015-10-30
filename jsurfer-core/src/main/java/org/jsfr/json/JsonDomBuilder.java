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

import java.util.Stack;

/**
 * Created by Leo on 2015/4/2.
 */
public class JsonDomBuilder implements JsonSaxHandler {

    private enum SCOPE {
        IN_OBJECT,
        IN_ARRAY,
        IN_ROOT
    }

    private SCOPE scope = SCOPE.IN_ROOT;
    private JsonProvider provider;
    private Stack<Object> stack = new Stack<Object>();
    private Object currentNode;
    private String propertyName;

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
        switch (scope) {
            case IN_OBJECT:
                provider.consumeObjectEntry(currentNode, propertyName, newObject);
                break;
            case IN_ARRAY:
                provider.consumeArrayElement(currentNode, newObject);
                break;
            case IN_ROOT:
                break;
        }
        scope = SCOPE.IN_OBJECT;
        stack.push(newObject);
        currentNode = newObject;
        propertyName = null;
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        switch (scope) {
            case IN_OBJECT:
                propertyName = key;
                break;
            case IN_ARRAY:
            case IN_ROOT:
                throw new IllegalStateException();
        }
        return true;
    }

    @Override
    public boolean endObject() {
        switch (scope) {
            case IN_OBJECT:
                stepOut();
                break;
            case IN_ARRAY:
            case IN_ROOT:
                throw new IllegalStateException();
        }
        return false;
    }

    private void stepOut() {
        stack.pop();
        if (!stack.isEmpty()) {
            currentNode = stack.peek();
            // TODO better way to determine scope?
            if (provider.isObject(currentNode)) {
                scope = SCOPE.IN_OBJECT;
            } else if (provider.isArray(currentNode)) {
                scope = SCOPE.IN_ARRAY;
            } else {
                throw new IllegalStateException();
            }
        } else {
            scope = SCOPE.IN_ROOT;
        }
    }

    @Override
    public boolean startArray() {
        Object newArray = provider.createArray();
        switch (scope) {
            case IN_OBJECT:
                provider.consumeObjectEntry(currentNode, propertyName, newArray);
                break;
            case IN_ARRAY:
                provider.consumeArrayElement(currentNode, newArray);
                break;
            case IN_ROOT:
                break;
        }
        scope = SCOPE.IN_ARRAY;
        stack.push(newArray);
        currentNode = newArray;
        return true;
    }

    @Override
    public boolean endArray() {
        stepOut();
        return true;
    }

    private void consumePrimitive(Object value) {
        switch (scope) {
            case IN_OBJECT:
                provider.consumeObjectEntry(currentNode, propertyName, value);
                break;
            case IN_ARRAY:
                provider.consumeArrayElement(currentNode, value);
                break;
            case IN_ROOT:
                currentNode = value;
                break;
        }
    }


    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        consumePrimitive(primitiveHolder.getValue());
        return true;
    }

    public boolean isInRoot() {
        return scope == SCOPE.IN_ROOT;
    }

    public Object getCurrentNode() {
        return currentNode;
    }

    public void clear() {
        propertyName = null;
        provider = null;
        stack = null;
        currentNode = null;
    }

}
