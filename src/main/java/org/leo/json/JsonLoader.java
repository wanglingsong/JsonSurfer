package org.leo.json;

import com.google.gson.Gson;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

public class JsonLoader {

    void load(final Reader reader) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        final Gson gson = new Gson();
        parser.parse(reader, new ContentHandler() {

            private ParsingContext context;

            @Override
            public void startJSON() throws ParseException, IOException {

            }

            @Override
            public void endJSON() throws ParseException, IOException {

            }

            @Override
            public boolean startObject() throws ParseException, IOException {
                return true;
            }

            @Override
            public boolean endObject() throws ParseException, IOException {
                return true;
            }

            @Override
            public boolean startObjectEntry(String key) throws ParseException, IOException {
                if ("bicycle".equals(key)) {

                }
                return true;
            }

            @Override
            public boolean endObjectEntry() throws ParseException, IOException {
                return true;
            }

            @Override
            public boolean startArray() throws ParseException, IOException {
                return true;
            }

            @Override
            public boolean endArray() throws ParseException, IOException {
                return true;
            }

            @Override
            public boolean primitive(Object value) throws ParseException, IOException {
                return true;
            }
        });

    }

    public static void main(String[] s) throws Exception{
        JsonLoader loader = new JsonLoader();
        String json = "{\n" +
                "    \"store\": {\n" +
                "        \"book\": [\n" +
                "            {\n" +
                "                \"category\": \"reference\",\n" +
                "                \"author\": \"Nigel Rees\",\n" +
                "                \"title\": \"Sayings of the Century\",\n" +
                "                \"price\": 8.95\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"Evelyn Waugh\",\n" +
                "                \"title\": \"Sword of Honour\",\n" +
                "                \"price\": 12.99\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"Herman Melville\",\n" +
                "                \"title\": \"Moby Dick\",\n" +
                "                \"isbn\": \"0-553-21311-3\",\n" +
                "                \"price\": 8.99\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"J. R. R. Tolkien\",\n" +
                "                \"title\": \"The Lord of the Rings\",\n" +
                "                \"isbn\": \"0-395-19395-8\",\n" +
                "                \"price\": 22.99\n" +
                "            }\n" +
                "        ],\n" +
                "        \"bicycle\": {\n" +
                "            \"color\": \"red\",\n" +
                "            \"price\": 19.95\n" +
                "        }\n" +
                "    },\n" +
                "    \"expensive\": 10\n" +
                "}";

        loader.load(new StringReader(json));
    }

}
