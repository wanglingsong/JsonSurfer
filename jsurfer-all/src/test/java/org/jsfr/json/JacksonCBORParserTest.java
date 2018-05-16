package org.jsfr.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import org.jsfr.json.provider.JacksonProvider;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JacksonCBORParserTest extends JsonSurferTest {

    @Before
    public void setUp() throws Exception {
        provider = new JacksonProvider();
        surfer = new JsonSurfer(new JacksonParser(new CBORFactory()), provider);
    }

    @Override
    protected InputStream read(String resourceName) throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(this.readAsString(resourceName));
        CBORFactory f = new CBORFactory();
        ObjectMapper cborMapper = new ObjectMapper(f);
        byte[] cborData = cborMapper.writeValueAsBytes(node);
        return new ByteArrayInputStream(cborData);
    }

    @Override
    public void testCollectAllFromString() throws Exception {
        // skip non-byte-based source
    }

    @Override
    public void testCollectOneFromString() throws Exception {
        // skip non-byte-based source
    }

    @Override
    public void testWildcardAtRoot() throws Exception {
        // skip non-byte-based source
    }

}
