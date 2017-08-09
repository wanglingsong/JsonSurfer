package org.jsfr.json;

import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.provider.FastJsonProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by Leo on 2017/3/31.
 */
public class FastJsonParesrTest extends JsonSurferTest {

    @Override
    @Before
    public void setUp() throws Exception {
        provider = FastJsonProvider.INSTANCE;
        surfer = new JsonSurfer(FastJsonParser.INSTANCE, provider);
    }

    @Test
    public void testSurfingIterator() throws Exception {
        Iterator<Object> iterator = surfer.iterator(read("sample.json"), JsonPathCompiler.compile("$.store.book[*]"));
        int count = 0;
        while (iterator.hasNext()) {
            LOGGER.info("Iterator next: {}", iterator.next());
            count++;
        }
        assertEquals(4, count);
    }

    @Test
    public void testResumableParser() throws Exception {
        SurfingConfiguration config = surfer.configBuilder()
                .bind("$.store.book[0]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        LOGGER.info("First pause");
                        context.pause();
                    }
                })
                .bind("$.store.book[1]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        LOGGER.info("Second pause");
                        context.pause();
                    }
                }).build();
        ResumableParser parser = surfer.getResumableParser(read("sample.json"), config);
        assertFalse(parser.resume());
        LOGGER.info("Start parsing");
        parser.parse();
        LOGGER.info("Resume from the first pause");
        assertTrue(parser.resume());
        LOGGER.info("Resume from the second pause");
        assertTrue(parser.resume());
        LOGGER.info("Parsing stopped");
        assertFalse(parser.resume());
    }

}
