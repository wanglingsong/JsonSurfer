import com.google.common.io.Resources;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.leo.json.BuilderFactory;
import org.leo.json.GsonSurfer;
import org.leo.json.JsonSurfer;
import org.leo.json.ContentHandlerBuilder;
import org.leo.json.JsonSimpleSurfer;
import org.leo.json.parse.*;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.InputStreamReader;

import static org.leo.json.BuilderFactory.path;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class JsonSurferTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{new Object[]{new JsonSimpleSurfer(), new JsonSimpleProvider()}, new Object[]{new GsonSurfer(), new GsonProvider()}};
    }

    @Parameterized.Parameter(0)
    public JsonSurfer loader;

    @Parameterized.Parameter(1)
    public JsonProvider provider;

    @Test
    public void testSampleJson() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().child("store").child("book").index(0).child("category"), mockListener)
                .bind(path().child("store").child("book").index(0), mockListener)
                .bind(path().child("store").child("car"), mockListener)
                .bind(path().child("store").child("bicycle"), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());

        Object book = provider.createObject();
        provider.consumeObjectEntry(book, "category", provider.primitive("reference"));
        provider.consumeObjectEntry(book, "author", provider.primitive("Nigel Rees"));
        provider.consumeObjectEntry(book, "title", provider.primitive("Sayings of the Century"));
        provider.consumeObjectEntry(book, "price", provider.primitive(8.95));
        verify(mockListener).onValue(eq(book), any(ParsingContext.class));

        verify(mockListener).onValue(eq(provider.primitive("reference")), any(ParsingContext.class));

        Object cars = provider.createArray();
        provider.consumeArrayElement(cars, provider.primitive("ferrari"));
        provider.consumeArrayElement(cars, provider.primitive("lamborghini"));
        verify(mockListener).onValue(eq(cars), any(ParsingContext.class));

        Object bicycle = provider.createObject();
        provider.consumeObjectEntry(bicycle, "color", provider.primitive("red"));
        provider.consumeObjectEntry(bicycle, "price", provider.primitive(19.95d));
        verify(mockListener).onValue(eq(bicycle), any(ParsingContext.class));
    }

    @Test
    public void testStoppableParsing() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().child("store").child("book").index(0), mockListener)
                .bind(path().child("store").child("book").index(1), mockListener)
                .bind(path().child("store").child("book").index(2), mockListener)
                .bind(path().child("store").child("book").index(3), mockListener);

        Mockito.doNothing().when(mockListener)
                .onValue(anyObject(), Matchers.argThat(new TypeSafeMatcher<ParsingContext>() {

                    @Override
                    public boolean matchesSafely(ParsingContext parsingContext) {
                        parsingContext.stopParsing();
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));

        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(1))
                .onValue(anyObject(), any(ParsingContext.class));

    }

    @Test
    public void testChildNodeWildcard() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().child("store").anyChild(), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(3))
                .onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testanyIndex() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().child("store").child("book").anyIndex(), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(4))
                .onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testWildcardCombination() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().child("store").child("book").anyIndex().anyChild(), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(18)).onValue(anyObject(),
                any(ParsingContext.class));
    }

    @Test
    public void testParsingArray() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener wholeArray = Mockito.mock(JsonPathListener.class);
        JsonPathListener stringElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener numberElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener booleanElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener nullElement = Mockito.mock(JsonPathListener.class);
        JsonPathListener objectElement = Mockito.mock(JsonPathListener.class);

        builder.bind(path(), wholeArray);
        builder.bind(path().index(0), stringElement);
        builder.bind(path().index(1), numberElement);
        builder.bind(path().index(2), booleanElement);
        builder.bind(path().index(3), nullElement);
        builder.bind(path().index(4), objectElement);
        loader.surf(new InputStreamReader(Resources.getResource("array.json").openStream()), builder.build());
        Object object = provider.createObject();
        provider.consumeObjectEntry(object, "key", provider.primitive("value"));
        Object array = provider.createArray();
        provider.consumeArrayElement(array, provider.primitive("abc"));
        provider.consumeArrayElement(array, provider.primitive(8.88));
        provider.consumeArrayElement(array, provider.primitive(true));
        provider.consumeArrayElement(array, provider.primitive(null));
        provider.consumeArrayElement(array, object);
        verify(wholeArray).onValue(eq(array), any(ParsingContext.class));
        verify(stringElement).onValue(eq(provider.primitive("abc")), any(ParsingContext.class));
        verify(numberElement).onValue(eq(provider.primitive(8.88)), any(ParsingContext.class));
        verify(booleanElement).onValue(eq(provider.primitive(true)), any(ParsingContext.class));
        verify(nullElement).onValue(eq(provider.primitive(null)), any(ParsingContext.class));
        verify(objectElement).onValue(eq(object), any(ParsingContext.class));

    }

    @Test
    public void testDeepScan() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().scan().child("author"), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive("Nigel Rees")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Herman Melville")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("J. R. R. Tolkien")), any(ParsingContext.class));
    }

    @Test
    public void testDeepScan2() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().child("store").scan().child("price"), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive(8.95)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(12.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(8.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(22.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(19.95)), any(ParsingContext.class));
    }

    @Test
    public void testFindEveryThing() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        builder.bind(path().scan().any(), new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                System.out.println(value);
            }
        });
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testIndexesAndChildrenOperator() throws Exception {
        ContentHandlerBuilder builder = BuilderFactory.handler().setJsonProvider(provider);
        JsonPathListener mockListener = Mockito.mock(JsonPathListener.class);
        builder.bind(path().scan().child("book").indexes(1,3).children("author", "title"), mockListener);
        loader.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Sword of Honour")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("J. R. R. Tolkien")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("The Lord of the Rings")), any(ParsingContext.class));
    }

}
