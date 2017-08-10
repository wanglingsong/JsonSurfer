package org.jsfr.json;

/**
 * Created by Leo on 2017/8/10.
 */
public interface NonBlockingParser {

    boolean feed(byte[] bytes, int start, int end);

    void endOfInput();

}
