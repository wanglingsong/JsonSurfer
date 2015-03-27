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

package org.leo.json.parse;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Leo on 2015/3/27.
 */
public class JavaCollectionProvider implements JsonProvider<Map<String, Object>, ArrayList<Object>, Object> {
    // TODO
    @Override
    public Map<String, Object> createObject() {
        return null;
    }

    @Override
    public ArrayList<Object> createArray() {
        return null;
    }

    @Override
    public boolean isObject(Object object) {
        return false;
    }

    @Override
    public boolean isArray(Object array) {
        return false;
    }

    @Override
    public void consumeObjectEntry(Map<String, Object> object, String key, Object value) {

    }

    @Override
    public void consumeArrayElement(ArrayList<Object> array, Object value) {

    }

    @Override
    public Object primitive(Object value) {
        return null;
    }
}
