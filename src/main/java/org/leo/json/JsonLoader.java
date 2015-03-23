package org.leo.json;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Administrator on 2015/3/23.
 */
public interface JsonLoader {

    void load(Reader reader, ValueBinder valueBinder) throws IOException, ParseException;

}
