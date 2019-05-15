package org.jsfr.json;

import java.util.Collection;

public class DispatchUtil {

    public static void dispatchValueToListeners(Object value, Collection<JsonPathListener> listeners, ParsingContext context, ErrorHandlingStrategy errorHandlingStrategy) {
        for (JsonPathListener listener : listeners) {
            if (context.isStopped()) {
                break;
            }
            try {
                listener.onValue(value, context);
            } catch (Exception e) {
                errorHandlingStrategy.handleExceptionFromListener(e, context);
            }
        }
    }

    public static void dispatchValueToListeners(Object value, JsonPathListener[] listeners, ParsingContext context, ErrorHandlingStrategy errorHandlingStrategy) {
        for (JsonPathListener listener : listeners) {
            if (context.isStopped()) {
                break;
            }
            try {
                listener.onValue(value, context);
            } catch (Exception e) {
                errorHandlingStrategy.handleExceptionFromListener(e, context);
            }
        }
    }

}
