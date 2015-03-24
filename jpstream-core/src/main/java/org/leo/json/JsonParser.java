package org.leo.json;

import java.io.IOException;
import java.io.Reader;

import org.json.simple.parser.ParseException;

/**
 * Created by Administrator on 2015/3/23.
 */
public interface JsonParser {

    JsonPathBinder binder();

    void parse(Reader reader, JsonPathBinder jsonPathBinder) throws IOException, ParseException;

}
