package org.jsfr.json;

import org.jsfr.json.provider.JsonSimpleProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Leo on 2017/8/26.
 */
public class JsonSimpleParserTest extends JsonSurferTest {

    @Before
    public void setUp() throws Exception {
        provider = JsonSimpleProvider.INSTANCE;
        surfer = new JsonSurfer(JsonSimpleParser.INSTANCE, provider);
    }

    @Override
    public void testTypeBindingOne() throws Exception {
        // ignore
    }

    @Ignore
    @Override
    public void testTypeBindingCollection() throws Exception {
        // ignore
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testSurfingIterator() throws Exception {
        super.testSurfingIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testResumableParser() throws Exception {
        super.testResumableParser();
    }

}
