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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkParseLongText {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkParseLongText.class);

    private JsonSurfer gsonSurfer;
    private JsonSurfer jacksonSurfer;
    private JsonSurfer simpleSurfer;
    private SurfingConfiguration surfingConfiguration;
    private CollectOneListener collectOneListener;
    private Gson gson;
    private ObjectMapper om;
    private String json;

    @Setup
    public void setup() throws Exception {
        gsonSurfer = JsonSurfer.gson();
        jacksonSurfer = JsonSurfer.jackson();
        simpleSurfer = JsonSurfer.simple();
        collectOneListener = new CollectOneListener(true);
        surfingConfiguration = SurfingConfiguration.builder().bind("$.findMe", collectOneListener).build();
        gson = new GsonBuilder().create();
        om = new ObjectMapper();
        json = Resources.toString(Resources.getResource("longText.json"), StandardCharsets.UTF_8);
    }

    @Benchmark
    public Object benchmarkGson() {
        Object value = gson.fromJson(json, Map.class).get("findMe");
        LOGGER.trace("Find me: {}", value);
        return value;
    }

    @Benchmark
    public Object benchmarkJackson() throws IOException {
        Object value = om.readTree(json).get("findMe");
        LOGGER.trace("Find me: {}", value);
        return value;
    }

    @Benchmark
    public Object benchmarkGsonSurfer() {
        gsonSurfer.surf(json, surfingConfiguration);
        Object value = collectOneListener.getValue();
        LOGGER.trace("Find me: {}", value);
        return value;
    }

    @Benchmark
    public Object benchmarkJacksonSurfer() {
        jacksonSurfer.surf(json, surfingConfiguration);
        Object value = collectOneListener.getValue();
        LOGGER.trace("Find me: {}", value);
        return value;
    }

    @Benchmark
    public Object benchmarkSimpleSurfer() {
        simpleSurfer.surf(json, surfingConfiguration);
        Object value = collectOneListener.getValue();
        LOGGER.trace("Find me: {}", value);
        return value;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkParseLongText.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
