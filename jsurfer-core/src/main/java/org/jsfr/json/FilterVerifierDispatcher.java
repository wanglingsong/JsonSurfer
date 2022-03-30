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

package org.jsfr.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterVerifierDispatcher extends ContentDispatcher {

    private final Map<SurfingConfiguration.Binding, List<JsonFilterVerifier>> verifiers = new HashMap<>();

    public void addVerifier(SurfingConfiguration.Binding binding, JsonFilterVerifier verifier) {
        this.addReceiver(verifier);
        if (binding.dependency != null) {
            List<JsonFilterVerifier> dependencies = this.verifiers.get(binding.dependency);
            if (dependencies != null) {
                List<JsonFilterVerifier> filtered = dependencies.stream().filter(d -> d.getStartDepth() < verifier.getStartDepth()).collect(Collectors.toList());
                verifier.setDependencies(filtered);
            }
        }
        this.verifiers.compute(binding, (binding1, prev) -> {
            List<JsonFilterVerifier> verifiers;
            if (prev == null) {
                verifiers = new ArrayList<>();
            } else {
                verifiers = prev;
            }
            verifiers.add(verifier);
            return verifiers;
        });
//        this.verifiers.put(binding, verifier);
    }

    public List<JsonPathListener> dispatch(JsonPosition jsonPosition, SurfingConfiguration.Binding binding) {
        int pathDepth = jsonPosition.pathDepth();
        SurfingConfiguration.Binding dependency = binding.dependency;
        JsonPathListener[] listeners = binding.getListeners();
        // TODO match relative path after dependency according to dependency start path and dispatch
        List<JsonPathListener> rst = new ArrayList<>();
        List<JsonFilterVerifier> dependencies = this.verifiers.get(dependency);
        if (dependencies != null) {
            for (JsonFilterVerifier verifier : dependencies) {
                if (binding.jsonPath.matchFilterPathUntilDepth(jsonPosition, verifier.getStartDepth())) {
                    if (verifier.getStartDepth() <= pathDepth) {
                        for (JsonPathListener listener : listeners) {
                            rst.add(verifier.addListener(listener));
                        }
                    }
                }
            }
        }
        return rst;
    }

    @Override
    protected void onRemove(JsonSaxHandler item) {
        for (Map.Entry<SurfingConfiguration.Binding, List<JsonFilterVerifier>> entry : this.verifiers.entrySet()) {
            entry.getValue().remove(item);
        }
    }
//    public JsonFilterVerifier getVerifier(SurfingConfiguration.Binding binding) {
//        return this.verifiers.get(binding);
//    }

}
