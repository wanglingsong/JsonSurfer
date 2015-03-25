package org.leo.json;

import org.leo.json.parse.ParsingContextImpl;

public class BuilderFactory {

    public static ContentHandlerBuilder start() {
        return new ParsingContextImpl();
    }

}
