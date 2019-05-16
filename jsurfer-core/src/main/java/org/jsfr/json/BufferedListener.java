package org.jsfr.json;

import java.util.LinkedList;

public class BufferedListener implements JsonPathListener {

    private SurfingConfiguration config;

    private JsonPathListener underlyingListener;

    private LinkedList<BufferedListener.ValueAndContext> buffer;

    public BufferedListener(SurfingConfiguration config, JsonPathListener underlyingListener) {
        this.config = config;
        this.underlyingListener = underlyingListener;
        this.buffer = new LinkedList<>();
    }

    @Override
    public void onValue(Object value, ParsingContext context) {
        buffer.add(new BufferedListener.ValueAndContext(value, context)); // buffer if we haven't verified filter yet
    }

    public void invokeBufferedValue() {
        while (!buffer.isEmpty()) {
            BufferedListener.ValueAndContext valueAndContext = buffer.poll();
            if (valueAndContext.context.isStopped()) {
                break;
            }
            try {
                underlyingListener.onValue(valueAndContext.value, valueAndContext.context);
            } catch (Exception e) {
                config.getErrorHandlingStrategy().handleExceptionFromListener(e, valueAndContext.context);
            }
        }
    }

    class ValueAndContext {
        Object value;
        ParsingContext context;

        ValueAndContext(Object value, ParsingContext context) {
            this.value = value;
            this.context = context;
        }
    }

}
