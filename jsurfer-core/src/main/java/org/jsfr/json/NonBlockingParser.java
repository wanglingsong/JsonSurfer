package org.jsfr.json;

/**
 * Interface for non-blocking parsing
 */
public interface NonBlockingParser extends ResumableParser {

    /**
     * Feed data and start or resume parsing immediately
     *
     * @param bytes bytes to feed
     * @param start start position
     * @param end end position
     * @return true if all feed data is successfully consumed
     */
    boolean feed(byte[] bytes, int start, int end);

    /**
     * Called to notify parser the input ended
     */
    void endOfInput();

}
