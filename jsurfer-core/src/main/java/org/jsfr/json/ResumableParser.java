package org.jsfr.json;

/**
 * Resumable Parser
 */
public interface ResumableParser {

    /**
     * Start parsing. It should be invoked only once!
     */
    void parse();

    /**
     * Resume parsing. It should not be invoked before parse()!
     *
     * @return true if parser was in paused state before invoking resume()
     */
    boolean resume();

}
