/*
 * MIT License
 *
 * Copyright (c) 2019 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jsfr.json;

import java.util.LinkedList;

public class BufferedListener implements JsonPathListener {

    private SurfingConfiguration config;

    private JsonPathListener underlyingListener;

    private LinkedList<BufferedListener.ValueAndContext> buffer;

    public BufferedListener(SurfingConfiguration config, JsonPathListener underlyingListener) {
        this.config = config;
        this.underlyingListener = underlyingListener;
        this.buffer = new LinkedList<>();
    }

    @Override
    public void onValue(Object value, ParsingContext context) {
        buffer.add(new BufferedListener.ValueAndContext(value, context)); // buffer if we haven't verified filter yet
    }

    public void invokeBufferedValue() {
        while (!buffer.isEmpty()) {
            BufferedListener.ValueAndContext valueAndContext = buffer.poll();
            if (valueAndContext.context.isStopped()) {
                break;
            }
            try {
                underlyingListener.onValue(valueAndContext.value, valueAndContext.context);
            } catch (Exception e) {
                config.getErrorHandlingStrategy().handleExceptionFromListener(e, valueAndContext.context);
            }
        }
    }

    class ValueAndContext {
        Object value;
        ParsingContext context;

        ValueAndContext(Object value, ParsingContext context) {
            this.value = value;
            this.context = context;
        }
    }

}
