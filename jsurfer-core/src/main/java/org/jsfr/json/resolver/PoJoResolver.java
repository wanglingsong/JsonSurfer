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

package org.jsfr.json.resolver;

import org.jsfr.json.exception.ResolverException;

import java.lang.reflect.Field;
import java.util.List;

public class PoJoResolver implements DocumentResolver<Object, Object> {

    @Override
    public Object resolve(Object object, String field) {
        Object value;
        try {
            Field declaredField = object.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            value = declaredField.get(object);
        } catch (IllegalAccessException e) {
            throw new ResolverException("Failed to resolve field: " + field, e);
        } catch (NoSuchFieldException e) {
            throw new ResolverException("Failed to resolve field: " + field, e);
        }
        return value;
    }

    @Override
    public Object resolve(Object list, int index) {

        if (list instanceof List) {
            return ((List) list).get(index);
        } else if (list.getClass().isArray()) {
            // TODO should resolve array
        }
        throw new UnsupportedOperationException("Unsupported list object " + list);
    }

}
