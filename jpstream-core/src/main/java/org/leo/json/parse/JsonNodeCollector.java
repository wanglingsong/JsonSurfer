package org.leo.json.parse;

import java.io.IOException;
import java.util.Collection;

import org.json.simple.parser.ParseException;

/**
 * Created by Administrator on 2015/3/21.
 */
public class JsonNodeCollector extends Transformer {

    private Collection<JsonPathListener> jsonPathListeners;
    private ParsingContext context;

    public JsonNodeCollector(Collection<JsonPathListener> jsonPathListeners, ParsingContext context) {
        this.jsonPathListeners = jsonPathListeners;
        this.context = context;
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        super.endObject();
        if (getStackSize() == 1) {
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(getResult(), context);
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        super.endObject();
        if (getStackSize() == 1) {
            Object result = getResult();
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(result, context);
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (getStackSize() == 0) {
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(value, context);
                }
            }
            return false;
        }
        super.primitive(value);
        if (getStackSize() == 1) {
            Object result = getResult();
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(result, context);
                }
            }
            return false;
        }
        return true;
    }

    public Iterable<JsonPathListener> getJsonPathListeners() {
        return jsonPathListeners;
    }
}
