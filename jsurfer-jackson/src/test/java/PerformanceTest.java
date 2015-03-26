import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2015/3/23.
 */
public class PerformanceTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);

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
