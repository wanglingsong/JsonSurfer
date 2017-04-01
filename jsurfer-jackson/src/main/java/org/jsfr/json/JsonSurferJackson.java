package org.jsfr.json;

import org.jsfr.json.provider.JacksonProvider;

/**
 * Created by Leo on 2017/4/1.
 */
public class JsonSurferJackson {

    public final static JsonSurfer INSTANCE = new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);

}
