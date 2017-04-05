package org.jsfr.json;

import org.jsfr.json.provider.FastJsonProvider;
import org.junit.Before;

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

}
