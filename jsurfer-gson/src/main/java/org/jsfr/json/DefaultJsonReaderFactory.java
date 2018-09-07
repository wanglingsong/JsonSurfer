package org.jsfr.json;

import com.google.gson.stream.JsonReader;

import java.io.Reader;

public class DefaultJsonReaderFactory implements JsonReaderFactory {

    @Override
    public JsonReader createJsonReader(Reader reader) {
        return new JsonReader(reader);
    }

}
