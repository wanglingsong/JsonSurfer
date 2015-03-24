import static org.leo.json.path.JsonPath.buildPath;

import java.io.InputStreamReader;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.leo.json.GsonParser;
import org.leo.json.JsonLoader;
import org.leo.json.JsonLoaderSimple;
import org.leo.json.parse.JsonPathListener;
import org.leo.json.parse.ParsingContext;
import org.leo.json.JsonPathBinder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.io.Resources;

@RunWith(Parameterized.class)
public class JsonLoaderTest {

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[] { new JsonLoaderSimple(), new GsonParser() };
    }

    @Parameterized.Parameter public JsonLoader loader;

    @Test
    public void testSampleJson() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").array(0).child("category"), mockListener)
            .bind(buildPath().child("store").child("book").array(0), mockListener)
            .bind(buildPath().child("store").child("car"), mockListener)
            .bind(buildPath().child("store").child("bicycle"), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        Mockito.verify(mockListener).onValue(Matchers.eq("reference"), Matchers.any(ParsingContext.class));
        JSONObject book = new JSONObject();
        book.put("category", "reference");
        book.put("author", "Nigel Rees");
        book.put("title", "Sayings of the Century");
        book.put("price", 8.95);
        Mockito.verify(mockListener).onValue(Matchers.eq(book), Matchers.any(ParsingContext.class));
        JSONArray cars = new JSONArray();
        cars.add("ferrari");
        cars.add("lamborghini");
        Mockito.verify(mockListener).onValue(Matchers.eq(cars), Matchers.any(ParsingContext.class));
        JSONObject bicycle = new JSONObject();
        bicycle.put("color", "red");
        bicycle.put("price", 19.95d);
        Mockito.verify(mockListener).onValue(Matchers.eq(bicycle), Matchers.any(ParsingContext.class));
    }

    @Test
    public void testStoppableParsing() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").array(0), mockListener)
            .bind(buildPath().child("store").child("book").array(1), mockListener)
            .bind(buildPath().child("store").child("book").array(2), mockListener)
            .bind(buildPath().child("store").child("book").array(3), mockListener);

        Mockito.doNothing().when(mockListener)
            .onValue(Matchers.anyObject(), Matchers.argThat(new TypeSafeMatcher<ParsingContext>() {

                @Override
                public boolean matchesSafely(ParsingContext parsingContext) {
                    parsingContext.stopParsing();
                    return true;
                }

                @Override
                public void describeTo(Description description) {
                }
            }));

        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        Mockito.verify(mockListener, Mockito.times(1))
            .onValue(Matchers.anyObject(), Matchers.any(ParsingContext.class));

    }

    @Test
    public void testChildNodeWildcard() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").childWildcard(), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        Mockito.verify(mockListener, Mockito.times(3))
            .onValue(Matchers.anyObject(), Matchers.any(ParsingContext.class));
    }

    @Test
    public void testArrayWildcard() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").arrayWildcard(), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        Mockito.verify(mockListener, Mockito.times(4))
            .onValue(Matchers.anyObject(), Matchers.any(ParsingContext.class));
    }

    @Test
    public void testWildcardCombination() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").arrayWildcard().childWildcard(), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        Mockito.verify(mockListener, Mockito.times(18)).onValue(Matchers.anyObject(),
            Matchers.any(ParsingContext.class));
    }

}
