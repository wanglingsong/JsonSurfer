package org.leo.json;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.leo.json.exception.JsonParseException;
import org.leo.json.parse.JsonSimpleFactory;
import org.leo.json.parse.ParsingContextImpl;

import java.io.IOException;
import java.io.Reader;

public class JsonSimpleParser implements JsonParser {

    @Override
    public JsonPathBinder binder() {
        return new ParsingContextImpl(new JsonSimpleFactory());
    }

    @Override
    public void parse(Reader reader, ContentHandler contentHandler) {
        JSONParser parser = new JSONParser();
        try {
            parser.parse(reader, contentHandler);
        } catch (ParseException e) {
            throw new JsonParseException("ParseException during parsing", e);
        } catch (IOException e) {
            throw new JsonParseException("IOException during parsing", e);
        }
    }

}
