package org.jsfr.json;

/**
 * Created by Leo on 2017/8/10.
 */
public interface NonBlockingParser extends ResumableParser{

    boolean feed(byte[] bytes, int start, int end);

    void endOfInput();

}
