/*
 * The MIT License
 *
 * Copyright (c) 2015 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.google.common.io.Resources;
import org.junit.Test;
import org.leo.json.BuilderFactory;
import org.leo.json.GsonSurfer;
import org.leo.json.parse.GsonProvider;
import org.leo.json.parse.JsonPathListener;
import org.leo.json.parse.ParsingContext;
import org.leo.json.parse.SurfingContext;

import java.io.InputStreamReader;

import static org.leo.json.BuilderFactory.root;

/**
 * Created by Leo on 2015/3/29.
 */
public class CodeExample {

    private JsonPathListener print = new JsonPathListener() {
        @Override
        public void onValue(Object value, ParsingContext context) {
            System.out.println(value);
        }
    };

    private GsonSurfer surfer = new GsonSurfer();

    @Test
    public void testExample1() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().child("store").child("book").anyIndex().child("author"), print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample2() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().scan().child("author"), print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample3() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().child("store").any(), print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample4() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().child("store").scan().child("price"), print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample5() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().scan().child("book").index(2), print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample6() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().scan().child("book").indexes(0, 1), print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testStoppable() throws Exception {
        SurfingContext.Builder builder = BuilderFactory.context().withJsonProvider(new GsonProvider());
        builder.bind(root().scan().child("book").indexes(0, 1), new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext parsingContext) {
                parsingContext.stopParsing();
                System.out.println(value);
            }
        });
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }


}
