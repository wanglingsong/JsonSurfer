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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;

/**
 * Created by Leo on 2017/4/1.
 */
public class FastJsonProvider implements JsonProvider<JSONObject, JSONArray, Object> {

    public final static FastJsonProvider INSTANCE = new FastJsonProvider();

    private FastJsonProvider() {}

    @Override
    public Object resolve(JSONObject object, String key) {
        return object.get(key);
    }

    @Override
    public Object resolve(JSONArray array, int index) {
        return array.get(index);
    }

    @Override
    public JSONObject createObject() {
        return new JSONObject();
    }

    @Override
    public JSONArray createArray() {
        return new JSONArray();
    }

    @Override
    public void put(JSONObject object, String key, Object value) {
        object.put(key, value);
    }

    @Override
    public void add(JSONArray array, Object value) {
        array.add(value);
    }

    @Override
    public Object primitive(boolean value) {
        return value;
    }

    @Override
    public Object primitive(int value) {
        return value;
    }

    @Override
    public Object primitive(BigInteger value) {
        return value;
    }

    @Override
    public Object primitive(double value) {
        return value;
    }

    @Override
    public Object primitive(long value) {
        return value;
    }

    @Override
    public Object primitive(String value) {
        return value;
    }

    @Override
    public Object primitiveNull() {
        return null;
    }

    @Override
    public <T> T cast(Object value, Class<T> tClass) {
        if (value instanceof JSON) {
            return JSON.toJavaObject((JSON) value, tClass);
        } else {
            return tClass.cast(value);
        }
    }

}
