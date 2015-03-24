import com.google.common.io.Resources;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.leo.json.GsonParser;
import org.leo.json.JsonParser;
import org.leo.json.JsonPathBinder;
import org.leo.json.JsonSimpleParser;
import org.leo.json.parse.*;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.InputStreamReader;

import static org.leo.json.path.JsonPath.buildPath;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class JsonParserTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{new Object[]{new JsonSimpleParser(), new JsonSimpleFactory()}, new Object[]{new GsonParser(), new GsonStructureFactory()}};
    }

    @Parameterized.Parameter(0)
    public JsonParser loader;

    @Parameterized.Parameter(1)
    public JsonStructureFactory factory;

    @Test
    public void testSampleJson() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").array(0).child("category"), mockListener)
                .bind(buildPath().child("store").child("book").array(0), mockListener)
                .bind(buildPath().child("store").child("car"), mockListener)
                .bind(buildPath().child("store").child("bicycle"), mockListener);
        loader.parse(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder.build());

        Object book = factory.createObject();
        factory.consumeObjectEntry(book, "category", factory.primitive("reference"));
        factory.consumeObjectEntry(book, "author", factory.primitive("Nigel Rees"));
        factory.consumeObjectEntry(book, "title", factory.primitive("Sayings of the Century"));
        factory.consumeObjectEntry(book, "price", factory.primitive(8.95));
        verify(mockListener).onValue(eq(book), any(ParsingContext.class));

        verify(mockListener).onValue(eq(factory.primitive("reference")), any(ParsingContext.class));

        Object cars = factory.createArray();
        factory.consumeArrayElement(cars, factory.primitive("ferrari"));
        factory.consumeArrayElement(cars, factory.primitive("lamborghini"));
        verify(mockListener).onValue(eq(cars), any(ParsingContext.class));

        Object bicycle = factory.createObject();
        factory.consumeObjectEntry(bicycle, "color", factory.primitive("red"));
        factory.consumeObjectEntry(bicycle, "price", factory.primitive(19.95d));
        verify(mockListener).onValue(eq(bicycle), any(ParsingContext.class));
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

        loader.parse(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder.build());
        verify(mockListener, Mockito.times(1))
                .onValue(Matchers.anyObject(), any(ParsingContext.class));

    }

    @Test
    public void testChildNodeWildcard() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").childWildcard(), mockListener);
        loader.parse(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder.build());
        verify(mockListener, Mockito.times(3))
                .onValue(Matchers.anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testArrayWildcard() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").arrayWildcard(), mockListener);
        loader.parse(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder.build());
        verify(mockListener, Mockito.times(4))
                .onValue(Matchers.anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testWildcardCombination() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        binder.bind(buildPath().child("store").child("book").arrayWildcard().childWildcard(), mockListener);
        loader.parse(new InputStreamReader(Resources.getResource("sample.json").openStream()), binder.build());
        verify(mockListener, Mockito.times(18)).onValue(Matchers.anyObject(),
                any(ParsingContext.class));
    }

    @Test
    public void testParsingArray() throws Exception {
        JsonPathBinder binder = loader.binder();
        JsonPathListener wholeArray = Mockito.mock(JsonPathListener.class);
        JsonPathListener stringElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener numberElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener booleanElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener nullElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener objectElement = Mockito.mock(JsonPathListener.class);

        binder.bind(buildPath(), wholeArray);
        binder.bind(buildPath().array(0), stringElement);
        binder.bind(buildPath().array(1), numberElement);
        binder.bind(buildPath().array(2), booleanElement);
        binder.bind(buildPath().array(3), nullElement);
        binder.bind(buildPath().array(4), objectElement);
        loader.parse(new InputStreamReader(Resources.getResource("array.json").openStream()), binder.build());
        Object object = factory.createObject();
        factory.consumeObjectEntry(object, "key", factory.primitive("value"));
        Object array = factory.createArray();
        factory.consumeArrayElement(array, factory.primitive("abc"));
        factory.consumeArrayElement(array, factory.primitive(8.88));
        factory.consumeArrayElement(array, factory.primitive(true));
        factory.consumeArrayElement(array, factory.primitive(null));
        factory.consumeArrayElement(array, object);
        verify(wholeArray).onValue(eq(array), any(ParsingContext.class));
        verify(stringElement).onValue(eq(factory.primitive("abc")), any(ParsingContext.class));
        verify(numberElement).onValue(eq(factory.primitive(8.88)), any(ParsingContext.class));
        verify(booleanElement).onValue(eq(factory.primitive(true)), any(ParsingContext.class));
        verify(nullElement).onValue(eq(factory.primitive(null)), any(ParsingContext.class));
        verify(objectElement).onValue(eq(object), any(ParsingContext.class));


    }

}
