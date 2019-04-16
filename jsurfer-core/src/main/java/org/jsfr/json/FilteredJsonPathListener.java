package org.jsfr.json;

import java.util.LinkedList;

/**
 * Created by Leo on 2017/4/4.
 */
public class FilteredJsonPathListener implements JsonPathListener {

    private JsonPathListener underlyingListener;

    private ParsingContext context;

    private SurfingConfiguration config;

    private LinkedList<ValueAndContext> buffer;

    private boolean filterVerified = false;

    private boolean filterPassed = false;

    FilteredJsonPathListener(JsonPathListener underlyingListener, ParsingContext context, SurfingConfiguration config) {
        this.underlyingListener = underlyingListener;
        this.context = context;
        this.config = config;
        this.buffer = new LinkedList<>();
    }

    @Override
    public void onValue(Object value, ParsingContext context) {
        if (filterVerified) {
            if (filterPassed) {
                try {
                    underlyingListener.onValue(value, context); // immediately relay if filter passed
                } catch (Exception e) {
                    config.getErrorHandlingStrategy().handleExceptionFromListener(e, context);
                }
            }
        } else {
            buffer.add(new ValueAndContext(value, context)); // buffer if we haven't verified filter yet
        }
    }

    JsonPathListener getUnderlyingListener() {
        return underlyingListener;
    }

    /**
     * Use to notify listener that filter has been verified and whether it passed.
     */
    void filterVerified(boolean passed) {
        this.filterVerified = true;
        this.filterPassed = passed;
        if (passed) {
            // Empty buffer to next listener
            while (!buffer.isEmpty()) {
                ValueAndContext valueAndContext = buffer.poll();
                try {
                    underlyingListener.onValue(valueAndContext.value, valueAndContext.context);
                } catch (Exception e) {
                    config.getErrorHandlingStrategy().handleExceptionFromListener(e, context);
                }
            }
        } else {
            this.clear();
        }
    }

    void clear() {
        this.buffer.clear();
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
