package org.jsfr.json;

import org.jsfr.json.provider.FastJsonProvider;

/**
 * Created by Leo on 2017/4/1.
 */
public class JsonSurferFastJson {

    public final static JsonSurfer INSTANCE = new JsonSurfer(FastJsonParser.INSTANCE, FastJsonProvider.INSTANCE);

}
