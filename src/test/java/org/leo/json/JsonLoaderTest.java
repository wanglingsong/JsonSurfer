package org.leo.json;

import com.google.common.io.Resources;
import org.hamcrest.Description;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStreamReader;

import static org.leo.json.JsonPath.buildPath;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class JsonLoaderTest {


    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[]{new JsonLoaderSimple(), new GsonParser()};
    }
    @Parameterized.Parameter
    public JsonLoader loader;

    @Test
    public void testSampleJson() throws Exception {
        ValueBinder binder = JsonLoaderSimple.binder();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").array(0).child("category"), mockListener)
                .bind(buildPath().child("store").child("book").array(0), mockListener)
                .bind(buildPath().child("store").child("car"), mockListener)
                .bind(buildPath().child("store").child("bicycle"), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        verify(mockListener).onValue(eq("reference"), any(ParsingContext.class));
        JSONObject book = new JSONObject();
        book.put("category", "reference");
        book.put("author", "Nigel Rees");
        book.put("title", "Sayings of the Century");
        book.put("price", 8.95);
        verify(mockListener).onValue(eq(book), any(ParsingContext.class));
        JSONArray cars = new JSONArray();
        cars.add("ferrari");
        cars.add("lamborghini");
        verify(mockListener).onValue(eq(cars), any(ParsingContext.class));
        JSONObject bicycle = new JSONObject();
        bicycle.put("color", "red");
        bicycle.put("price", 19.95d);
        verify(mockListener).onValue(eq(bicycle), any(ParsingContext.class));
    }

    @Test
    public void testStoppableParsing() throws Exception {
        ValueBinder binder = JsonLoaderSimple.binder();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").array(0), mockListener)
                .bind(buildPath().child("store").child("book").array(1), mockListener)
                .bind(buildPath().child("store").child("book").array(2), mockListener)
                .bind(buildPath().child("store").child("book").array(3), mockListener);

        doNothing().when(mockListener).onValue(anyObject(), argThat(new TypeSafeMatcher<ParsingContext>() {
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
        verify(mockListener, times(1)).onValue(anyObject(), any(ParsingContext.class));

    }

    @Test
    public void testChildNodeWildcard() throws Exception {
        ValueBinder binder = JsonLoaderSimple.binder();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").childWildcard(), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        verify(mockListener, times(3)).onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testArrayWildcard() throws Exception {
        ValueBinder binder = JsonLoaderSimple.binder();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").arrayWildcard(), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        verify(mockListener, times(4)).onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testWildcardCombination() throws Exception {
        ValueBinder binder = JsonLoaderSimple.binder();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").arrayWildcard().childWildcard(), mockListener);
        loader.load(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder);
        verify(mockListener, times(18)).onValue(anyObject(), any(ParsingContext.class));
    }

}