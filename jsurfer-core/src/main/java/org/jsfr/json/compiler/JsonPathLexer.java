// Generated from JsonPath.g4 by ANTLR 4.7

package org.jsfr.json.compiler;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonPathLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, COLON=16, AndOperator=17, 
		OrOperator=18, NUM=19, QUOTED_STRING=20, KEY=21, WS=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "COLON", "AndOperator", 
		"OrOperator", "NUM", "INT", "EXP", "QUOTED_STRING", "KEY", "ESC", "UNICODE", 
		"HEX", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'['", "']'", "','", "'.'", 
		"'[?('", "')]'", "'@'", "'>'", "'<'", "'=='", "':'", "'&&'", "'||'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "COLON", "AndOperator", "OrOperator", "NUM", "QUOTED_STRING", 
		"KEY", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public JsonPathLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JsonPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\u00b5\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\22\3\22\3\22\3\23\3\23\3\23\3\24\5\24i\n\24\3\24\3\24\3\24\6\24n\n"+
		"\24\r\24\16\24o\3\24\5\24s\n\24\3\24\5\24v\n\24\3\24\3\24\3\24\3\24\5"+
		"\24|\n\24\3\24\5\24\177\n\24\3\25\3\25\3\25\7\25\u0084\n\25\f\25\16\25"+
		"\u0087\13\25\5\25\u0089\n\25\3\26\3\26\5\26\u008d\n\26\3\26\3\26\3\27"+
		"\3\27\3\27\3\27\7\27\u0095\n\27\f\27\16\27\u0098\13\27\3\27\3\27\3\30"+
		"\3\30\6\30\u009e\n\30\r\30\16\30\u009f\3\31\3\31\3\31\5\31\u00a5\n\31"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\34\6\34\u00b0\n\34\r\34\16"+
		"\34\u00b1\3\34\3\34\2\2\35\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\2+\2-\26/\27\61\2\63"+
		"\2\65\2\67\30\3\2\13\3\2\62;\3\2\63;\4\2GGgg\4\2--//\4\2))^^\r\2\13\f"+
		"\17\17\"\"$$(,..\60\60<<>B]_~~\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\5\2"+
		"\13\f\17\17\"\"\2\u00bf\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\67\3\2\2\2\39\3\2\2\2\5;\3\2\2\2\7>\3\2\2\2\tA\3\2\2\2\13E\3\2\2"+
		"\2\rG\3\2\2\2\17I\3\2\2\2\21K\3\2\2\2\23M\3\2\2\2\25O\3\2\2\2\27S\3\2"+
		"\2\2\31V\3\2\2\2\33X\3\2\2\2\35Z\3\2\2\2\37\\\3\2\2\2!_\3\2\2\2#a\3\2"+
		"\2\2%d\3\2\2\2\'~\3\2\2\2)\u0088\3\2\2\2+\u008a\3\2\2\2-\u0090\3\2\2\2"+
		"/\u009d\3\2\2\2\61\u00a1\3\2\2\2\63\u00a6\3\2\2\2\65\u00ac\3\2\2\2\67"+
		"\u00af\3\2\2\29:\7&\2\2:\4\3\2\2\2;<\7\60\2\2<=\7\60\2\2=\6\3\2\2\2>?"+
		"\7\60\2\2?@\7,\2\2@\b\3\2\2\2AB\7]\2\2BC\7,\2\2CD\7_\2\2D\n\3\2\2\2EF"+
		"\7,\2\2F\f\3\2\2\2GH\7]\2\2H\16\3\2\2\2IJ\7_\2\2J\20\3\2\2\2KL\7.\2\2"+
		"L\22\3\2\2\2MN\7\60\2\2N\24\3\2\2\2OP\7]\2\2PQ\7A\2\2QR\7*\2\2R\26\3\2"+
		"\2\2ST\7+\2\2TU\7_\2\2U\30\3\2\2\2VW\7B\2\2W\32\3\2\2\2XY\7@\2\2Y\34\3"+
		"\2\2\2Z[\7>\2\2[\36\3\2\2\2\\]\7?\2\2]^\7?\2\2^ \3\2\2\2_`\7<\2\2`\"\3"+
		"\2\2\2ab\7(\2\2bc\7(\2\2c$\3\2\2\2de\7~\2\2ef\7~\2\2f&\3\2\2\2gi\7/\2"+
		"\2hg\3\2\2\2hi\3\2\2\2ij\3\2\2\2jk\5)\25\2km\7\60\2\2ln\t\2\2\2ml\3\2"+
		"\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2pr\3\2\2\2qs\5+\26\2rq\3\2\2\2rs\3\2"+
		"\2\2s\177\3\2\2\2tv\7/\2\2ut\3\2\2\2uv\3\2\2\2vw\3\2\2\2wx\5)\25\2xy\5"+
		"+\26\2y\177\3\2\2\2z|\7/\2\2{z\3\2\2\2{|\3\2\2\2|}\3\2\2\2}\177\5)\25"+
		"\2~h\3\2\2\2~u\3\2\2\2~{\3\2\2\2\177(\3\2\2\2\u0080\u0089\7\62\2\2\u0081"+
		"\u0085\t\3\2\2\u0082\u0084\t\2\2\2\u0083\u0082\3\2\2\2\u0084\u0087\3\2"+
		"\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0089\3\2\2\2\u0087"+
		"\u0085\3\2\2\2\u0088\u0080\3\2\2\2\u0088\u0081\3\2\2\2\u0089*\3\2\2\2"+
		"\u008a\u008c\t\4\2\2\u008b\u008d\t\5\2\2\u008c\u008b\3\2\2\2\u008c\u008d"+
		"\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\5)\25\2\u008f,\3\2\2\2\u0090"+
		"\u0096\7)\2\2\u0091\u0095\n\6\2\2\u0092\u0093\7^\2\2\u0093\u0095\13\2"+
		"\2\2\u0094\u0091\3\2\2\2\u0094\u0092\3\2\2\2\u0095\u0098\3\2\2\2\u0096"+
		"\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0099\3\2\2\2\u0098\u0096\3\2"+
		"\2\2\u0099\u009a\7)\2\2\u009a.\3\2\2\2\u009b\u009e\5\61\31\2\u009c\u009e"+
		"\n\7\2\2\u009d\u009b\3\2\2\2\u009d\u009c\3\2\2\2\u009e\u009f\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\60\3\2\2\2\u00a1\u00a4\7^\2\2"+
		"\u00a2\u00a5\t\b\2\2\u00a3\u00a5\5\63\32\2\u00a4\u00a2\3\2\2\2\u00a4\u00a3"+
		"\3\2\2\2\u00a5\62\3\2\2\2\u00a6\u00a7\7w\2\2\u00a7\u00a8\5\65\33\2\u00a8"+
		"\u00a9\5\65\33\2\u00a9\u00aa\5\65\33\2\u00aa\u00ab\5\65\33\2\u00ab\64"+
		"\3\2\2\2\u00ac\u00ad\t\t\2\2\u00ad\66\3\2\2\2\u00ae\u00b0\t\n\2\2\u00af"+
		"\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b4\b\34\2\2\u00b48\3\2\2\2\22\2horu"+
		"{~\u0085\u0088\u008c\u0094\u0096\u009d\u009f\u00a4\u00b1\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}