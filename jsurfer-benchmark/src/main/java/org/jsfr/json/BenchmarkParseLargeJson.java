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

package org.jsfr.json;

import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Created by lwang on 2015/7/16 0016.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkParseLargeJson {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkParseLargeJson.class);

    @Param({"$.builders.*.properties", "$.builders..properties"})
    private String jsonPath;

    private JsonSurfer simpleSurfer;
    private JsonSurfer gsonSurfer;
    private JsonSurfer jacksonSurfer;
    private JsonPathListener blackHoleListener;
    private SurfingConfiguration surfingConfiguration;
    private String json;

    @Setup
    public void setup() throws IOException {
        simpleSurfer = JsonSurfer.simple();
        gsonSurfer = JsonSurfer.gson();
        jacksonSurfer = JsonSurfer.jackson();
        blackHoleListener = new JsonPathListener() {
            private Blackhole blackhole = new Blackhole();

            @Override
            public void onValue(Object value, ParsingContext context) throws Exception {
                LOGGER.trace("Properties: {}", value);
                blackhole.consume(value);
            }
        };
        surfingConfiguration = SurfingConfiguration.builder().bind(jsonPath, blackHoleListener).skipOverlappedPath().build();
        json = Resources.toString(Resources.getResource("allthethings.json"), StandardCharsets.UTF_8);
    }

    @Benchmark
    public Object benchmarkSimpleSufer() {
        simpleSurfer.surf(json, surfingConfiguration);
        return null;
    }

    @Benchmark
    public Object benchmarkGsonSufer() {
        gsonSurfer.surf(json, surfingConfiguration);
        return null;
    }

    @Benchmark
    public Object benchmarkJacksonSufer() {
        jacksonSurfer.surf(json, surfingConfiguration);
        return null;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkParseLargeJson.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
