package org.jsfr.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import org.jsfr.json.provider.JacksonProvider;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JacksonSmileParserTest extends JsonSurferTest {

    @Before
    public void setUp() throws Exception {
        provider = new JacksonProvider();
        surfer = new JsonSurfer(new JacksonParser(new SmileFactory()), provider);
    }

    @Override
    protected InputStream read(String resourceName) throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(this.readAsString(resourceName));
        SmileFactory f = new SmileFactory();
        ObjectMapper cborMapper = new ObjectMapper(f);
        byte[] smileData = cborMapper.writeValueAsBytes(node);
        return new ByteArrayInputStream(smileData);
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
