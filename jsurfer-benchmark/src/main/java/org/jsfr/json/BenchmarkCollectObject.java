/*
 * The MIT License
 *
 * Copyright (c) 2017 WANG Lingsong
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

package org.jsfr.json;

import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkCollectObject {

    private JsonSurfer gsonSurfer;
    private JsonSurfer jacksonSurfer;
    private JsonSurfer simpleSurfer;
    private JsonSurfer fastjsonSurfer;
    private SurfingConfiguration surfingConfiguration;
    private String json;

    @Setup
    public void setup() throws Exception {
        gsonSurfer = JsonSurferGson.INSTANCE;
        jacksonSurfer = JsonSurferJackson.INSTANCE;
        simpleSurfer = JsonSurferJsonSimple.INSTANCE;
        fastjsonSurfer = JsonSurferFastJson.INSTANCE;
        TypedJsonPathListener collectOneListener = new TypedJsonPathListener() {

            private Blackhole blackhole = new Blackhole();

            @Override
            public void onTypedValue(Object value, ParsingContext context) {
                blackhole.consume(value);
            }
        };
        surfingConfiguration = SurfingConfiguration.builder().bind("$.store.book[*]", Map.class, collectOneListener).withCharset(StandardCharsets.UTF_8).build();
        json = Resources.toString(Resources.getResource("sample.json"), StandardCharsets.UTF_8);
    }


    @Benchmark
    public boolean benchmarkGsonWithJsonSurfer() {
        gsonSurfer.surf(json, surfingConfiguration);
        return true;
    }

    @Benchmark
    public boolean benchmarkJacksonWithJsonSurfer() {
        jacksonSurfer.surf(json, surfingConfiguration);
        return true;
    }

    @Benchmark
    public boolean benchmarkJsonSimpleWithJsonSurfer() {
        simpleSurfer.surf(json, surfingConfiguration);
        return true;
    }

    @Benchmark
    public boolean benchmarkFastJsonWithJsonSurfer() {
        fastjsonSurfer.surf(json, surfingConfiguration);
        return true;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkCollectObject.class.getSimpleName())
//                .addProfiler(FlightRecordingProfiler.class)
                .build();
        new Runner(opt).run();
    }

}
