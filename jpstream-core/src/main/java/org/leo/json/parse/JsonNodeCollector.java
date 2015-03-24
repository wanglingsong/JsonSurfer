package org.leo.json.parse;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Collection;

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
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        super.endArray();
        if (getStackSize() == 1) {
            Object result = getResult();
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(result, context);
                }
            }
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        super.primitive(value);
        if (getStackSize() == 1) {
            Object result = getResult();
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(result, context);
                }
            }
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        this.context = null;
        this.jsonPathListeners = null;
    }


}
