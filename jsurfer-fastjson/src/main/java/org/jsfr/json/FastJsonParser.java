package org.jsfr.json;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.parser.JSONScanner;

import java.io.Reader;

import static com.alibaba.fastjson.parser.JSONToken.*;

/**
 * Created by Leo on 2017/3/31.
 */
public class FastJsonParser implements JsonParserAdapter {

    public final static FastJsonParser INSTANCE = new FastJsonParser();

    private FastJsonParser() {
    }

    @Override
    public void parse(Reader reader, SurfingContext context) {
        doParse(new JSONReaderScanner(reader), context);
    }

    @Override
    public void parse(String json, SurfingContext context) {
        doParse(new JSONScanner(json), context);
    }

    private void doParse(JSONLexerBase lexer, SurfingContext context) {

        try {
            StaticPrimitiveHolder staticPrimitiveHolder = new StaticPrimitiveHolder();
            context.startJSON();
            String tempString = null;
            do {
                lexer.nextToken();
                int token = lexer.token();
                //System.out.println("token: " + token);
                if (tempString != null) {
                    if (token == COLON) {
                        context.startObjectEntry(tempString);
                    } else {
                        context.primitive(staticPrimitiveHolder.withValue(tempString));
                    }
                    tempString = null;
                }
                switch (token) {
                    case SET:
                    case TREE_SET:
                    case NEW:
                        break;
                    case COMMA:
                    case COLON:
                        break;
                    case LBRACKET:
                        context.startArray();
                        break;
                    case RBRACKET:
                        context.endArray();
                        break;
                    case LBRACE:
                        context.startObject();
                        break;
                    case RBRACE:
                        context.endObject();
                        break;
                    case LITERAL_INT:
                        context.primitive(staticPrimitiveHolder.withValue(lexer.longValue()));
                        break;
                    case LITERAL_FLOAT:
                        // Number value = lexer.decimalValue(lexer.isEnabled(Feature.UseBigDecimal));
                        context.primitive(staticPrimitiveHolder.withValue(lexer.doubleValue()));
                        break;
                    case IDENTIFIER:
                    case LITERAL_STRING:
                        tempString = lexer.stringVal();
                        break;
                    case NULL:
                    case UNDEFINED:
                        context.primitive(staticPrimitiveHolder.withValue(null));
                        break;
                    case TRUE:
                        context.primitive(staticPrimitiveHolder.withValue(true));
                        break;
                    case FALSE:
                        context.primitive(staticPrimitiveHolder.withValue(false));
                        break;
                    case EOF:
                        context.endJSON();
                        break;
                    case ERROR:
                    default:
                        throw new JSONException("syntax error, " + lexer.info());
                }
            } while (!lexer.isEOF() && !context.isStopped());

            if (tempString != null) {
                context.primitive(staticPrimitiveHolder.withValue(tempString));
            }
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
        }

    }

}
