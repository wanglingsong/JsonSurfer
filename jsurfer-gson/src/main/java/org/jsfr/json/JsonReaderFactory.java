package org.jsfr.json;

import com.google.gson.stream.JsonReader;

import java.io.Reader;

public interface JsonReaderFactory {

    JsonReader createJsonReader(Reader reader);

}
