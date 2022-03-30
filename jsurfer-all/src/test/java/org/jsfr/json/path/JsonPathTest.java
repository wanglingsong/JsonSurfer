/*
 * MIT License
 *
 * Copyright (c) 2019 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jsfr.json.path;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jsfr.json.Book;
import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.provider.JavaCollectionProvider;
import org.jsfr.json.resolver.PoJoResolver;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.jsfr.json.compiler.JsonPathCompiler.compile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonPathTest {

    @Test
    public void shallResolvePath() throws Exception {
        String json = Resources.toString(Resources.getResource("sample2.json"), StandardCharsets.UTF_8);
        Object retrieved = compile("$[0].calculationAgent.cconsol").resolve(new Gson().fromJson(json, Object.class), JavaCollectionProvider.INSTANCE);
        assertEquals("59999", retrieved);
    }

    @Test
    public void shallResolvePOJO() throws Exception {
        Book book = new Book();
        book.setAuthor("Leo");
        book.setCategory("Fiction");
        book.setPrice(100.0d);
        book.setTitle("JsonSurfer is great!");
        assertEquals("Leo", compile("$.author").resolve(book, new PoJoResolver()));
        assertEquals("Fiction", compile("$.category").resolve(book, new PoJoResolver()));
        assertEquals(100.0d, compile("$.price").resolve(book, new PoJoResolver()));
        assertEquals("JsonSurfer is great!", compile("$.title").resolve(book, new PoJoResolver()));

        List<String> list = Arrays.asList("foo", "bar");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        assertEquals("bar", compile("$[1]").resolve(list, new PoJoResolver()));
        assertEquals("bar", compile("$.list[1]").resolve(map, JavaCollectionProvider.INSTANCE));

        int[] array = new int[]{12, 34};
        assertEquals(34, compile("$[1]").resolve(array, new PoJoResolver()));
        assertEquals(12, compile("$[0]").resolve(array, new PoJoResolver()));

    }

    @Test
    public void shallEnsureJsonPathCapacity() throws Exception {
        JsonPath path = compile("$.a.b.c.d.e.f.g.h.a.b.c.d.e.f.g.h.a.b.c.d.e.f.g.h.a.b.c.d.e.f.g.h.a.b.c.d.e.f.g.h");
        assertEquals(41, path.size);
    }

//    @Test
//    public void shallMatch() throws Exception {
//        JsonPath path1 = compile("$..store..book..book[?(@.category=='fiction')]..title");
//        JsonPath path2 = compile("$..store..book.store.book[?(@.category=='fiction')]..title");
//        JsonPath path3 = compile("$..store..book.store[?(@.category=='fiction')]..title");
//        JsonPath path4 = compile("$..book..book..book..store.book[?(@.category=='fiction')]..title");
//        JsonPath position = compile("$.book.store.book.store.book[1].volumes[1].title");
//        assertTrue(path1.matchWithDeepScan(position));
//        assertTrue(path2.matchWithDeepScan(position));
//        assertFalse(path3.matchWithDeepScan(position));
//        assertFalse(path4.matchWithDeepScan(position));
//    }

    @Test
    public void testJsonPathFilterMatchRegexInputMismatch() throws Exception {
        try {
            JsonPathCompiler.compile("$.store.book[?(@.author=~ /abc)]"); // not a valid regular expression
        } catch (ParseCancellationException e) {
            assertTrue(e.getCause() instanceof InputMismatchException);
        }
    }

}