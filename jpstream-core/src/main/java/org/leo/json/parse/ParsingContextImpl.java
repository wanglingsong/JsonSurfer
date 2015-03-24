package org.leo.json.parse;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;
import org.leo.json.JsonPathBinder;
import org.leo.json.path.ArrayIndex;
import org.leo.json.path.JsonPath;
import org.leo.json.path.PathOperator;
import org.leo.json.path.PathOperator.Type;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ParsingContextImpl implements ParsingContext, JsonPathBinder, ContentHandler {

    private boolean stopped = false;
    private JsonPath currentPath;
    private Map<Integer, Map<JsonPath, JsonPathListener[]>> listenerMap = Maps.newHashMap();
    private ParsingObserver observer = new ParsingObserver();

    private interface CollectorProcessor {

        boolean process(JsonNodeCollector collector) throws IOException, ParseException;

    }

    private CollectorProcessor startJsonProcessor = new CollectorProcessor() {

        @Override
        public boolean process(JsonNodeCollector collector) throws IOException, ParseException {
            collector.startJSON();
            return true;
        }
    };

    private CollectorProcessor startObjectProcessor = new CollectorProcessor() {

        @Override
        public boolean process(JsonNodeCollector collector) throws IOException, ParseException {
            collector.startJSON();
            collector.startObject();
            return true;
        }
    };

    private CollectorProcessor startArrayProcessor = new CollectorProcessor() {

        @Override
        public boolean process(JsonNodeCollector collector) throws IOException, ParseException {
            collector.startJSON();
            collector.startArray();
            return true;
        }
    };

    @Override
    public void startJSON() throws ParseException, IOException {
        if (stopped) {
            return;
        }
        observer.startJSON();
        currentPath = JsonPath.buildPath();
        doMatching(startJsonProcessor);
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        if (stopped) {
            return;
        }
        observer.endJSON();
        currentPath.clear();
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.startObject();
        PathOperator top = currentPath.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(startObjectProcessor);
        }
        return true;
    }

    private void doMatching(CollectorProcessor processor) throws IOException, ParseException {
        // TODO support only definite path
        int semiHashcode = semiHashcode(currentPath);
        Map<JsonPath, JsonPathListener[]> map = listenerMap.get(semiHashcode);
        if (map == null) {
            return;
        }
        LinkedList<JsonPathListener> listeners = null;
        for (Map.Entry<JsonPath, JsonPathListener[]> entry : map.entrySet()) {
            if (entry.getKey().match(currentPath)) {
                if (listeners == null) {
                    listeners = Lists.newLinkedList();
                }
                Collections.addAll(listeners, entry.getValue());
            }
        }
        if (listeners != null) {
            JsonNodeCollector collector = new JsonNodeCollector(listeners, this);
            if (processor.process(collector)) {
                observer.addObserver(collector);
            }
        }
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.startObjectEntry(key);
        currentPath.child(key);
        doMatching(startJsonProcessor);
        return true;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.endObjectEntry();
        currentPath.pop();
        return true;
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.startArray();
        PathOperator top = currentPath.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(startArrayProcessor);
        }
        currentPath.array();
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.endArray();
        currentPath.pop();
        return true;
    }

    @Override
    public boolean primitive(final Object value) throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        observer.primitive(value);
        PathOperator top = currentPath.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            final ParsingContextImpl self = this;
            doMatching(new CollectorProcessor() {

                @Override
                public boolean process(JsonNodeCollector collector) throws IOException, ParseException {
                    for (JsonPathListener jsonPathListener : collector.getJsonPathListeners()) {
                        jsonPathListener.onValue(value, self);
                    }
                    return false;
                }
            });

        }
        return true;
    }

    @Override
    public String getPath() {
        return this.currentPath.toString();
    }

    @Override
    public ContentHandler build() {
        this.listenerMap = Collections.unmodifiableMap(this.listenerMap);
        return this;
    }

    @Override
    public JsonPathBinder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners) {
        int semiHashcode = semiHashcode(jsonPath);
        Map<JsonPath, JsonPathListener[]> map = listenerMap.get(semiHashcode);
        if (map == null) {
            map = Maps.newHashMap();
            listenerMap.put(semiHashcode, map);
        }
        map.put(jsonPath, jsonPathListeners);
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
