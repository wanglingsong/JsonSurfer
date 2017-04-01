package org.jsfr.json;

import org.jsfr.json.provider.JsonSimpleProvider;

/**
 * Created by Leo on 2017/4/1.
 */
public class JsonSurferJsonSimple {

    public final static JsonSurfer INSTANCE = new JsonSurfer(JsonSimpleParser.INSTANCE, JsonSimpleProvider.INSTANCE);

}
