package org.leo.json;

import org.json.simple.parser.ContentHandler;
import org.leo.json.parse.JsonPathListener;
import org.leo.json.parse.JsonProvider;
import org.leo.json.path.JsonPath;

/**
 * Created by Administrator on 2015/3/22.
 */
public interface ContentHandlerBuilder {

    ContentHandlerBuilder setJsonStructureFactory(JsonProvider structureFactory);

    ContentHandlerBuilder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners);

    ContentHandler build();

}
