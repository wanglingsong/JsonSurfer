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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/3/21.
 */
class ContentDispatcher implements JsonSaxHandler {

    LinkedList<JsonSaxHandler> receiver = new LinkedList<JsonSaxHandler>();

    public boolean isEmpty() {
        return this.receiver.isEmpty();
    }

    @Override
    public boolean startJSON() {
        if (receiver.isEmpty()) {
            return true;
        }
        for (JsonSaxHandler observer : receiver) {
            observer.startJSON();
        }
        return true;
    }

    @Override
    public boolean endJSON() {
        if (receiver.isEmpty()) {
            return true;
        }
        for (JsonSaxHandler observer : receiver) {
            observer.endJSON();
        }
        return true;
    }

    @Override
    public boolean startObject() {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<JsonSaxHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            JsonSaxHandler observer = itr.next();
            if (!observer.startObject()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endObject() {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<JsonSaxHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            JsonSaxHandler observer = itr.next();
            if (!observer.endObject()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<JsonSaxHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            JsonSaxHandler observer = itr.next();
            if (!observer.startObjectEntry(key)) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean startArray() {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<JsonSaxHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            JsonSaxHandler observer = itr.next();
            if (!observer.startArray()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endArray() {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<JsonSaxHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            JsonSaxHandler observer = itr.next();
            if (!observer.endArray()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<JsonSaxHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            JsonSaxHandler observer = itr.next();
            if (!observer.primitive(value)) {
                itr.remove();
            }
        }
        return true;
    }

    public void addReceiver(JsonSaxHandler contentHandler) {
        receiver.add(contentHandler);
    }
}
