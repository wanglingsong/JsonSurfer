/*
 * The MIT License
 *
 * Copyright (c) 2015 WANG Lingsong
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.jsfr.json.JacksonSurfer;
import org.jsfr.json.parse.JacksonProvider;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Leo on 2015/3/30.
 */
public class JacksonSurferTest extends JsonSurferTest {

    @Override
    @Before
    public void setUp() throws Exception {
        surfer = new JacksonSurfer();
        provider = new JacksonProvider();
    }

    @Test
    public void testLargeJsonJackson() throws Exception {
        final AtomicLong counter = new AtomicLong();
        ObjectMapper om = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(Resources.getResource("allthethings.json").openStream());
        long start = System.currentTimeMillis();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        while (jp.nextToken() == JsonToken.FIELD_NAME) {
            if (jp.nextToken() == JsonToken.START_OBJECT) {
                TreeNode tree = om.readTree(jp);
                counter.incrementAndGet();
                LOGGER.trace("value: {}", tree);
            }
        }
        jp.close();
        LOGGER.info("Jackson processes {} value in {} millisecond", counter.get(), System.currentTimeMillis() - start);
    }

}
