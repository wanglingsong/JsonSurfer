package org.jsfr.json;

import org.jsfr.json.provider.GsonProvider;

/**
 * Created by Leo on 2017/4/1.
 */
public class JsonSurferGson {

    public final static JsonSurfer INSTANCE = new JsonSurfer(GsonParser.INSTANCE, GsonProvider.INSTANCE);

}
