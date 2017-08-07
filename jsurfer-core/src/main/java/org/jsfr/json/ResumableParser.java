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
     * @return true if parser is not stopped and in paused state before calling resume()
     */
    boolean resume();

}
