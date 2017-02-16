/*
 * The MIT License
 *
 * Copyright (c) 2017 WANG Lingsong
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

package org.jsfr.json.path;

import org.jsfr.json.resolver.DocumentResolver;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ArrayIndex extends PathOperator {

    private int arrayIndex = -1;

    public ArrayIndex() {
    }

    public ArrayIndex(int arrayIndex) {
        this();
        this.arrayIndex = arrayIndex;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public void reset() {
        this.arrayIndex = -1;
    }

    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public void increaseArrayIndex() {
        this.arrayIndex++;
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return super.match(pathOperator) && arrayIndex == ((ArrayIndex) pathOperator).arrayIndex;
    }

    @Override
    public Object resolve(Object document, DocumentResolver resolver) {
        return resolver.resolve(document, arrayIndex);
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public String toString() {
        return "[" + arrayIndex + "]";
    }
}
