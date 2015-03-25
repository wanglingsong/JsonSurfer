package org.leo.json;

import org.json.simple.parser.ContentHandler;

import java.io.Reader;

/**
 * Created by Administrator on 2015/3/23.
 */
public interface JsonParser {

    void parse(Reader reader, ContentHandler contentHandler);

}
