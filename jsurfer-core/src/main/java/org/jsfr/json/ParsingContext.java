/*
 * The MIT License
 *
 * Copyright (c) 2017 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jsfr.json;

/**
 * Created by Leo on 2015/3/22.
 */
public interface ParsingContext {

    /**
     * Stop parsing
     */
    void stop();

    /**
     * @return Whether parsing is stopped
     */
    boolean isStopped();

    /**
     * Pause parsing
     */
    void pause();

    /**
     * Resume parsing
     */
    void resume();

    /**
     * @return Whether parsing is paused
     */
    boolean isPaused();

    /**
     * @return The current JsonPath
     */
    String getJsonPath();

    /**
     * @return The current field name. Null if current position is not in json object
     */
    String getCurrentFieldName();

    /**
     * @return The current index of json array. -1 if current position is not in json array
     */
    int getCurrentArrayIndex();

    /**
     * Save a transient data during parsing
     *
     * @param key   key
     * @param value value
     */
    void save(String key, Object value);

    /**
     * @param key    key
     * @param tClass class to cast
     * @param <T>    type
     * @return Saved value
     */
    <T> T load(String key, Class<T> tClass);

}
