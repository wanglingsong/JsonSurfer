package org.leo.json;

import java.io.IOException;
import java.io.Reader;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.leo.json.parse.ParsingContextImpl;

public class JsonLoaderSimple implements JsonLoader {

    @Override
    public JsonPathBinder binder() {
        return new ParsingContextImpl();
    }

    public void load(Reader reader, ContentHandler contentHandler) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        parser.parse(reader, contentHandler);
    }

    @Override
    public void load(Reader reader, JsonPathBinder jsonPathBinder) throws IOException, ParseException {
        this.load(reader, jsonPathBinder.build());
    }

}
