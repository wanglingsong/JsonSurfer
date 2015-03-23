package org.leo.json;

import org.json.simple.parser.ContentHandler;

/**
 * Created by Administrator on 2015/3/22.
 */
public interface ValueBinder {

    ValueBinder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners);

    ContentHandler build();

}
