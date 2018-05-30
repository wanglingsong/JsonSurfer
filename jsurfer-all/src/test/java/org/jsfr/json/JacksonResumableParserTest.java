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

package org.jsfr.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JacksonProvider;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class JacksonResumableParserTest extends JsonSurferTest {

    @Before
    public void setUp() {
        provider = new JacksonProvider();
        surfer = new JsonSurfer(JacksonParser.INSTANCE, provider);
    }

    @Test
    public void testParsingLongIntegers() {
        BigInteger expected = new BigInteger("3452453534534534534543534543545");
        JsonPath jsonPath = JsonPathCompiler.compile("$");
        Iterator<Object> iterator = surfer.iterator("{\"value\": " + expected.toString() + "}", jsonPath);
        Object parsedResult = iterator.next();
        assertEquals(0, ((ObjectNode) parsedResult).get("value").bigIntegerValue().compareTo(expected));
    }
}
