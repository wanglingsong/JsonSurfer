package org.jsfr.json;

import org.jsfr.json.provider.JacksonProvider;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

/**
 * Created by Leo on 2017/4/1.
 */
public class JsonSurferJackson {

    public final static JsonSurfer INSTANCE = new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);

    public static JsonSurfer createSurfer(JsonFactory factory) {
      return new JsonSurfer(new JacksonParser(factory), JacksonProvider.INSTANCE);
    }
}
