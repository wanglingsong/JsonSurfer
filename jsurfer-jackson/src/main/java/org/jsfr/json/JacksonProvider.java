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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by Leo on 2015/3/29.
 */
public class JacksonProvider implements JsonProvider<ObjectNode, ArrayNode, JsonNode> {

    private JsonNodeFactory factory = JsonNodeFactory.instance;

    @Override
    public ObjectNode createObject() {
        return factory.objectNode();
    }

    @Override
    public ArrayNode createArray() {
        return factory.arrayNode();
    }

    @Override
    public boolean isObject(Object object) {
        return object instanceof ObjectNode;
    }

    @Override
    public boolean isArray(Object array) {
        return array instanceof ArrayNode;
    }

    @Override
    public void consumeObjectEntry(ObjectNode object, String key, JsonNode value) {
        object.set(key, value);
    }

    @Override
    public void consumeArrayElement(ArrayNode array, JsonNode value) {
        array.add(value);
    }

    @Override
    public JsonNode primitive(boolean value) {
        return factory.booleanNode(value);
    }

    @Override
    public JsonNode primitive(int value) {
        return factory.numberNode(value);
    }

    @Override
    public JsonNode primitive(double value) {
        return factory.numberNode(value);
    }

    @Override
    public JsonNode primitive(String value) {
        return factory.textNode(value);
    }

    @Override
    public JsonNode primitiveNull() {
        return factory.nullNode();
    }

}
