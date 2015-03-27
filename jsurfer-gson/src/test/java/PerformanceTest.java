import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.leo.json.HandlerBuilder;
import org.leo.json.GsonSurfer;
import org.leo.json.JsonSimpleSurfer;
import org.leo.json.parse.GsonProvider;
import org.leo.json.parse.JsonPathListener;
import org.leo.json.parse.JsonSimpleProvider;
import org.leo.json.parse.ParsingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.leo.json.BuilderFactory.handler;
import static org.leo.json.BuilderFactory.path;


public class PerformanceTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);

    @Test
    public void testLargeJsonJPSimple() throws Exception {
        JsonSimpleSurfer loader = new JsonSimpleSurfer();
        HandlerBuilder handler = handler().setJsonProvider(new JsonSimpleProvider());
        final AtomicLong counter = new AtomicLong();
        JsonPathListener printListener = new JsonPathListener() {

            @Override
            public void onValue(Object value, ParsingContext context) {
                counter.incrementAndGet();
                LOGGER.trace("value: {}", value);
            }
        };
        handler.bind(path().child("builders").anyChild().child("properties"), printListener).skipOverlappedPath();
        long start = System.currentTimeMillis();
        loader.surf(new InputStreamReader(Resources.getResource("allthethings.json").openStream()), handler.build());
        LOGGER.info("jsurfer-simple processes {} value in {} millisecond", counter.get(), System.currentTimeMillis()
                - start);

    }

    @Test
    public void testLargeJsonJPGson() throws Exception {
        GsonSurfer loader = new GsonSurfer();
        HandlerBuilder handler = handler().setJsonProvider(new GsonProvider());
        final AtomicLong counter = new AtomicLong();
        JsonPathListener printListener = new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                counter.incrementAndGet();
                LOGGER.trace("value: {}", value);
            }
        };
        handler.bind(path().child("builders").anyChild().child("properties"), printListener).skipOverlappedPath();
        long start = System.currentTimeMillis();
        loader.surf(new InputStreamReader(Resources.getResource("allthethings.json").openStream()), handler.build());
        LOGGER.info("jsurfer-gson processes {} value in {} millisecond", counter.get(), System.currentTimeMillis() - start);

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
