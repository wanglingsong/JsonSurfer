package org.leo.json;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;

public class JsonLoaderSimple implements JsonLoader {

    public static ValueBinder binder() {
        return new ParsingContextImpl();
    }

    public void load(Reader reader, ContentHandler contentHandler) throws IOException, ParseException {
        // TODO rewrite json parser or use gson
        JSONParser parser = new JSONParser();
        parser.parse(reader, contentHandler);
    }

    @Override
    public void load(Reader reader, ValueBinder valueBinder) throws IOException, ParseException {
        this.load(reader, valueBinder.build());
    }

}
