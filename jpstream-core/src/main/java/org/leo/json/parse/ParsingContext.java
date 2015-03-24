package org.leo.json.parse;

/**
 * Created by Administrator on 2015/3/22.
 */
public interface ParsingContext {

    void stopParsing();

    boolean isStopped();

    String getPath();

}
