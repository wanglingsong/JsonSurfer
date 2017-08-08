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

    private static class FastJsonResumableParser implements ResumableParser {

        private JSONLexerBase lexer;
        private SurfingContext context;
        private StaticPrimitiveHolder staticPrimitiveHolder;

        public FastJsonResumableParser(JSONLexerBase lexer, SurfingContext context, StaticPrimitiveHolder staticPrimitiveHolder) {
            this.lexer = lexer;
            this.context = context;
            this.staticPrimitiveHolder = staticPrimitiveHolder;
        }

        @Override
        public void parse() {
            context.startJSON();
            doParse();
        }

        @Override
        public boolean resume() {
            try {
                if (context.isStopped() || !context.isPaused()) {
                    return false;
                }
                context.resume();
                doParse();
                return true;
            } catch (Exception e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
                return false;
            }
        }

        private void doParse() {

            try {
                String tempString = null;

                while (!lexer.isEOF() && !context.isStopped() && !context.isPaused()) {
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
                }
                ;

                if (tempString != null) {
                    context.primitive(staticPrimitiveHolder.withValue(tempString));
                }
            } catch (Exception e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            }

        }
    }

    public final static FastJsonParser INSTANCE = new FastJsonParser();

    private FastJsonParser() {
    }

    @Override
    public void parse(Reader reader, SurfingContext context) {
        createParser(reader, context).parse();
    }

    @Override
    public void parse(String json, SurfingContext context) {
        createParser(json, context).parse();
    }

    @Override
    public ResumableParser createParser(Reader reader, SurfingContext context) {
        return new FastJsonResumableParser(new JSONReaderScanner(reader), context, new StaticPrimitiveHolder());
    }

    @Override
    public ResumableParser createParser(String json, SurfingContext context) {
        return new FastJsonResumableParser(new JSONScanner(json), context, new StaticPrimitiveHolder());
    }

}
