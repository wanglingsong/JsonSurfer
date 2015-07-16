package org.jsfr.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by lwang on 16/7/2015.
 */
@State(Scope.Benchmark)
public class JsonSurferBenchmark {

    private JsonSurfer jsonSurfer;
    private SurfingConfiguration surfingConfiguration;
    private Gson gson;
    private String json;

    public JsonSurferBenchmark() {
        jsonSurfer = JsonSurfer.gson();
        gson = new GsonBuilder().create();
        surfingConfiguration = SurfingConfiguration.builder().bind("$.store.book[0].author", new JsonPathListener() {

            @Override
            public void onValue(Object value, ParsingContext context) throws Exception {
                context.stopParsing();
            }
        }).build();
        try {
            json = Resources.toString(Resources.getResource("sample.json"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void benchmarkJsonSurfer() {
        jsonSurfer.surf(json, surfingConfiguration);
    }

    @Benchmark
    public void benchmarkGson() {
        gson.fromJson(json, Map.class);
    }

    public static void main(String[] args) throws RunnerException {
        // TODO choose a profiler
        Options opt = new OptionsBuilder()
            .include(JsonSurferBenchmark.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }

}
