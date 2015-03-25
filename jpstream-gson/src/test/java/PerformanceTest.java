import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.leo.json.GsonParser;
import org.leo.json.ContentHandlerBuilder;
import org.leo.json.JsonSimpleParser;
import org.leo.json.parse.JsonPathListener;
import org.leo.json.parse.ParsingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.leo.json.path.JsonPath.start;

public class PerformanceTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);

    @Test
    public void testLargeJsonJPSimple() throws Exception {
        JsonSimpleParser loader = new JsonSimpleParser();
        ContentHandlerBuilder binder = JsonSimpleParser.start();
        final AtomicLong counter = new AtomicLong();
        JsonPathListener printListener = new JsonPathListener() {

            @Override
            public void onValue(Object value, ParsingContext context) {
                counter.incrementAndGet();
                LOGGER.trace("value: {}", value);
            }
        };
        binder.bind(start().child("builders").childWildcard().child("properties"), printListener);
        long start = System.currentTimeMillis();
        loader.parse(new InputStreamReader(Resources.getResource("allthethings.json").openStream()), binder.build());
        LOGGER.info("JPStream-simple processes {} value in {} millisecond", counter.get(), System.currentTimeMillis()
                - start);

    }

    @Test
    public void testLargeJsonJPGson() throws Exception {
        GsonParser loader = new GsonParser();
        ContentHandlerBuilder binder = GsonParser.start();
        final AtomicLong counter = new AtomicLong();
        JsonPathListener printListener = new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                counter.incrementAndGet();
                LOGGER.trace("value: {}", value);
            }
        };
        binder.bind(start().child("builders").childWildcard().child("properties"), printListener);
        long start = System.currentTimeMillis();
        loader.parse(new InputStreamReader(Resources.getResource("allthethings.json").openStream()), binder.build());
        LOGGER.info("JPStream-gson processes {} value in {} millisecond", counter.get(), System.currentTimeMillis() - start);

    }

    @Test
    public void testLargeJsonRawGson() throws Exception {
        final AtomicLong counter = new AtomicLong();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(Resources.getResource("allthethings.json").openStream(), "UTF-8"));
        long start = System.currentTimeMillis();
        reader.beginObject();
        reader.nextName();
        // $.builders
        reader.beginObject();
        while (reader.hasNext()) {
            reader.nextName();
            Map element = gson.fromJson(reader, Map.class);
            Object value = ((Map) element.get("properties")).get("branch");
            counter.incrementAndGet();
            LOGGER.trace("JsonPath: {} value: {}", reader.getPath(), value);
        }
        LOGGER.info("Gson processes {} value in {} millisecond", counter.get(), System.currentTimeMillis() - start);
    }

}
