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

package org.jsfr.json.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsfr.json.exception.JsonSurfingException;

import java.math.BigInteger;

public class JacksonProvider implements JsonProvider<ObjectNode, ArrayNode, JsonNode> {

    public static final JacksonProvider INSTANCE = new JacksonProvider();

    private ObjectMapper om;

    private JsonNodeFactory factory;

    public JacksonProvider() {
        this(new ObjectMapper());
    }

    public JacksonProvider(ObjectMapper om) {
        this.om = om;
        this.factory = om.getNodeFactory();
    }

    @Override
    public ObjectNode createObject() {
        return factory.objectNode();
    }

    @Override
    public ArrayNode createArray() {
        return factory.arrayNode();
    }

    @Override
    public void put(ObjectNode object, String key, JsonNode value) {
        object.set(key, value);
    }

    @Override
    public void add(ArrayNode array, JsonNode value) {
        array.add(value);
    }

    @Override
    public Object resolve(ObjectNode object, String key) {
        return object.get(key);
    }

    @Override
    public Object resolve(ArrayNode array, int index) {
        return array.get(index);
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
    public JsonNode primitive(BigInteger value) {
        return factory.numberNode(value);
    }

    @Override
    public JsonNode primitive(double value) {
        return factory.numberNode(value);
    }

    @Override
    public JsonNode primitive(long value) {
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

    @Override
    public <T> T cast(JsonNode value, Class<T> tClass) {
        try {
            if (value == null) {
                return null;
            } else if (om.canDeserialize(om.getTypeFactory().constructType(tClass))) {
                return om.treeToValue(value, tClass);
            } else {
                return tClass.cast(value);
            }
        } catch (JsonProcessingException e) {
            throw new JsonSurfingException(e);
        }
    }

}
