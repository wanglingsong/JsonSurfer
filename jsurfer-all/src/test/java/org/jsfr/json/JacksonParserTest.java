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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;
import org.apache.avro.Schema;
import org.jsfr.json.provider.JacksonProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Leo on 2015/3/30.
 */
public class JacksonParserTest extends JsonSurferTest {

    @Before
    public void setUp() throws Exception {
        provider = new JacksonProvider();
        surfer = new JsonSurfer(JacksonParser.INSTANCE, provider);
    }

    @Test
    public void testNonBlockingParser() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        SurfingConfiguration config = surfer.configBuilder()
                .bind("$['foo','bar']", mockListener)
                .build();
        byte[] part1 = "{\"foo\": 12".getBytes("UTF-8");
        byte[] part2 = "34, \"bar\": \"ab".getBytes("UTF-8");
        byte[] part3 = "cd\"}".getBytes("UTF-8");

        NonBlockingParser nonBlockingParser = surfer.createNonBlockingParser(config);
        assertTrue(nonBlockingParser.feed(part1, 0, part1.length));
        assertTrue(nonBlockingParser.feed(part2, 0, part2.length));
        assertTrue(nonBlockingParser.feed(part3, 0, part3.length));
        nonBlockingParser.endOfInput();
        assertFalse(nonBlockingParser.feed(part1, 0, 100));
        verify(mockListener).onValue(eq(provider.primitive(1234L)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("abcd")), any(ParsingContext.class));
    }

    @Test
    @Ignore
    public void testProtobufParser() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);

        ObjectMapper mapper = new ProtobufMapper();
        String protobuf_str = "message Employee {\n"
                + " required string name = 1;\n"
                + " required int32 age = 2;\n"
                + " repeated string emails = 3;\n"
                + " optional Employee boss = 4;\n"
                + "}\n";
        final ProtobufSchema schema = ProtobufSchemaLoader.std.parse(protobuf_str);

        Employee empl = new Employee();
        empl.age = 30;
        empl.emails = new String[]{"foo@gmail.com"};
        empl.name = "foo";

        byte[] protobufData = mapper.writer(schema)
                .writeValueAsBytes(empl);
        // TODO Jackson's bug to be fixed in 2.9.6
        JsonSurfer protobufSurfer = new JsonSurfer(new JacksonParser(new ProtobufFactory(), schema), provider);
        SurfingConfiguration config = protobufSurfer.configBuilder().bind("$.name", mockListener).build();
        protobufSurfer.surf(new ByteArrayInputStream(protobufData), config);
        verify(mockListener).onValue(eq(provider.primitive("foo")), any(ParsingContext.class));
    }

    @Test
    public void testAvroParser() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);

        String SCHEMA_JSON = "{\n"
                + "\"type\": \"record\",\n"
                + "\"name\": \"Employee\",\n"
                + "\"fields\": [\n"
                + " {\"name\": \"name\", \"type\": \"string\"},\n"
                + " {\"name\": \"age\", \"type\": \"int\"},\n"
                + " {\"name\": \"emails\", \"type\": {\"type\": \"array\", \"items\": \"string\"}},\n"
                + " {\"name\": \"boss\", \"type\": [\"Employee\",\"null\"]}\n"
                + "]}";
        Schema raw = new Schema.Parser().setValidate(true).parse(SCHEMA_JSON);
        final AvroSchema schema = new AvroSchema(raw);

        Employee empl = new Employee();
        empl.age = 30;
        empl.emails = new String[]{"foo@gmail.com"};
        empl.name = "foo";

        AvroMapper mapper = new AvroMapper();
        byte[] avroData = mapper.writer(schema)
                .writeValueAsBytes(empl);

        JsonSurfer avroSurfer = new JsonSurfer(new JacksonParser(new AvroFactory(), schema), provider);
        SurfingConfiguration config = avroSurfer.configBuilder().bind("$.name", mockListener).build();
        avroSurfer.surf(new ByteArrayInputStream(avroData), config);
        verify(mockListener).onValue(eq(provider.primitive("foo")), any(ParsingContext.class));
    }

    @Test
    public void testParsingLongIntegers() throws Exception {
        Collector collector = surfer.collector(read("sample_big_integer.json"));
        ValueBox<Object> box = collector.collectOne("$.value");
        collector.exec();
        BigInteger expected = new BigInteger("3452453534534534534543534543545");
        assertEquals(expected, box.get());
    }

}
