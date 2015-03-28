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

package org.leo.json.parse;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/3/21.
 */
public class ContentDispatcher implements ContentHandler {

    LinkedList<ContentHandler> receiver = new LinkedList<ContentHandler>();

    public boolean isEmpty() {
        return this.receiver.isEmpty();
    }

    @Override
    public void startJSON() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return;
        }
        for (ContentHandler observer : receiver) {
            observer.startJSON();
        }
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return;
        }
        for (ContentHandler observer : receiver) {
            observer.endJSON();
        }
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.startObject()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.endObject()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.startObjectEntry(key)) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.endObjectEntry()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.startArray()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.endArray()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (receiver.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = receiver.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.primitive(value)) {
                itr.remove();
            }
        }
        return true;
    }

    public void addReceiver(ContentHandler contentHandler) {
        receiver.add(contentHandler);
    }
}
