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

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;
import org.leo.json.ContentHandlerBuilder;
import org.leo.json.path.ArrayIndex;
import org.leo.json.path.JsonPath;
import org.leo.json.path.PathOperator;
import org.leo.json.path.PathOperator.Type;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SurfingContext implements ParsingContext, ContentHandlerBuilder, ContentHandler {

    private boolean built = false;
    private boolean stopped = false;
    private boolean skipOverlappedPath = false;
    private JsonPosition currentPosition;
    private Map<Integer, Map<JsonPath, JsonPathListener[]>> definitePathMap = Maps.newHashMap();
    private Map<JsonPath, JsonPathListener[]> indefinitePathMap = Maps.newHashMap();
    private ContentDispatcher dispatcher = new ContentDispatcher();
    private JsonProvider jsonProvider;

    private interface CollectorProcessor {

        boolean process(JsonCollector collector) throws IOException, ParseException;

    }

    private CollectorProcessor startJsonProcessor = new CollectorProcessor() {

        @Override
        public boolean process(JsonCollector collector) throws IOException, ParseException {
            collector.startJSON();
            return true;
        }
    };

    @Override
    public ContentHandlerBuilder skipOverlappedPath() {
        this.skipOverlappedPath = true;
        return this;
    }

    @Override
    public ContentHandlerBuilder setJsonProvider(JsonProvider structureFactory) {
        this.jsonProvider = structureFactory;
        return this;
    }

    @Override
    public void startJSON() throws ParseException, IOException {
        if (stopped) {
            return;
        }
        currentPosition = JsonPosition.start();
        doMatching(null);
        dispatcher.startJSON();
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        if (stopped) {
            return;
        }
        dispatcher.endJSON();
        // clear resources
        currentPosition.clear();
        currentPosition = null;
        indefinitePathMap = null;
        definitePathMap = null;
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(startJsonProcessor);
        }
        dispatcher.startObject();
        return true;
    }

    private void doMatching(CollectorProcessor processor) throws IOException, ParseException {
        // skip matching if "skipOverlappedPath" is enable
        if (skipOverlappedPath && !this.dispatcher.isEmpty()) {
            return;
        }
        LinkedList<JsonPathListener> listeners = null;

        if (!indefinitePathMap.isEmpty()) {
            for (Map.Entry<JsonPath, JsonPathListener[]> entry : indefinitePathMap.entrySet()) {
                if (entry.getKey().match(currentPosition)) {
                    if (listeners == null) {
                        listeners = Lists.newLinkedList();
                    }
                    Collections.addAll(listeners, entry.getValue());
                }
            }
        }

        int semiHashcode = semiHashcode(currentPosition);
        Map<JsonPath, JsonPathListener[]> map = definitePathMap.get(semiHashcode);
        if (map != null) {
            for (Map.Entry<JsonPath, JsonPathListener[]> entry : map.entrySet()) {
                if (entry.getKey().match(currentPosition)) {
                    if (listeners == null) {
                        listeners = Lists.newLinkedList();
                    }
                    Collections.addAll(listeners, entry.getValue());
                }
            }
        }

        if (listeners != null) {
            JsonCollector collector = new JsonCollector(listeners, this);
            collector.setProvider(jsonProvider);
            if (processor != null) {
                processor.process(collector);
            }
            dispatcher.addReceiver(collector);
        }
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        dispatcher.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        currentPosition.stepInObject(key);
        dispatcher.startObjectEntry(key);
        doMatching(startJsonProcessor);
        return true;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        currentPosition.pop();
        dispatcher.endObjectEntry();
        return true;
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(startJsonProcessor);
        }
        currentPosition.stepInArray();
        dispatcher.startArray();
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        currentPosition.pop();
        dispatcher.endArray();
        return true;
    }

    @Override
    public boolean primitive(final Object value) throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(startJsonProcessor);
        }
        dispatcher.primitive(value);
        return true;
    }

    @Override
    public String getPath() {
        return this.currentPosition.toString();
    }

    @Override
    public ContentHandler build() {
        this.built = true;
        return this;
    }

    @Override
    public ContentHandlerBuilder bind(JsonPath.Builder builder, JsonPathListener... jsonPathListeners) {
        return bind(builder.build(), jsonPathListeners);
    }

    @Override
    public ContentHandlerBuilder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners) {
        if (built) {
            throw new IllegalStateException("The JsonSurfer is already built");
        }
        if (!jsonPath.isDefinite()) {
            indefinitePathMap.put(jsonPath, jsonPathListeners);
        } else {
            int semiHashcode = semiHashcode(jsonPath);
            Map<JsonPath, JsonPathListener[]> map = definitePathMap.get(semiHashcode);
            if (map == null) {
                map = Maps.newHashMap();
                definitePathMap.put(semiHashcode, map);
            }
            map.put(jsonPath, jsonPathListeners);
        }
        return this;
    }

    @Override
    public void stopParsing() {
        this.stopped = true;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    private int semiHashcode(JsonPath path) {
        return 31 * path.pathDepth() + path.peek().getType().ordinal();
    }
}
