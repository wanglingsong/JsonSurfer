package org.leo.json;

import java.io.IOException;
import java.io.Reader;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.leo.json.exception.JsonParseException;
import org.leo.json.parse.JsonSimpleFactory;

public class JsonSimpleParser implements JsonParser {

    public static ContentHandlerBuilder start() {
        return BuilderFactory.start().setJsonStructureFactory(new JsonSimpleFactory());
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
