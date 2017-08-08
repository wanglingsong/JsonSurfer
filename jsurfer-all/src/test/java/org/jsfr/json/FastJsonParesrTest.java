package org.jsfr.json;

import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.provider.FastJsonProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

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
        Iterator iterator = surfer.iterator(read("sample.json"), JsonPathCompiler.compile("$.store.book[*]"));
        int count = 0;
        while (iterator.hasNext()) {
            LOGGER.info("Iterator next: {}", iterator.next());
            count++;
        }
        assertEquals(4, count);
    }

}
