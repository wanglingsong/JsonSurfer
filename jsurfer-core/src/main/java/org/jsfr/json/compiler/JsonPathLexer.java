// Generated from JsonPath.g4 by ANTLR 4.7.1

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
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		COLON=18, NegationOperator=19, AndOperator=20, OrOperator=21, NUM=22, 
		QUOTED_STRING=23, KEY=24, WS=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"COLON", "NegationOperator", "AndOperator", "OrOperator", "NUM", "INT", 
		"EXP", "QUOTED_STRING", "KEY", "ESC", "UNICODE", "HEX", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'['", "']'", "','", "'.'", 
		"'[?('", "')]'", "'('", "')'", "'@'", "'>'", "'<'", "'=='", "':'", "'!'", 
		"'&&'", "'||'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "COLON", "NegationOperator", "AndOperator", 
		"OrOperator", "NUM", "QUOTED_STRING", "KEY", "WS"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\33\u00c1\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2"+
		"\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17"+
		"\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\3\27\5\27u\n\27\3\27\3\27\3\27\6\27z\n\27\r\27\16"+
		"\27{\3\27\5\27\177\n\27\3\27\5\27\u0082\n\27\3\27\3\27\3\27\3\27\5\27"+
		"\u0088\n\27\3\27\5\27\u008b\n\27\3\30\3\30\3\30\7\30\u0090\n\30\f\30\16"+
		"\30\u0093\13\30\5\30\u0095\n\30\3\31\3\31\5\31\u0099\n\31\3\31\3\31\3"+
		"\32\3\32\3\32\3\32\7\32\u00a1\n\32\f\32\16\32\u00a4\13\32\3\32\3\32\3"+
		"\33\3\33\6\33\u00aa\n\33\r\33\16\33\u00ab\3\34\3\34\3\34\5\34\u00b1\n"+
		"\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\37\6\37\u00bc\n\37\r\37"+
		"\16\37\u00bd\3\37\3\37\2\2 \3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\2\61\2\63"+
		"\31\65\32\67\29\2;\2=\33\3\2\13\3\2\62;\3\2\63;\4\2GGgg\4\2--//\4\2))"+
		"^^\f\2\13\f\17\17\"$(,..\60\60<<>B]_~~\n\2$$\61\61^^ddhhppttvv\5\2\62"+
		";CHch\5\2\13\f\17\17\"\"\2\u00cb\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2=\3\2\2\2\3?\3\2\2\2"+
		"\5A\3\2\2\2\7D\3\2\2\2\tG\3\2\2\2\13K\3\2\2\2\rM\3\2\2\2\17O\3\2\2\2\21"+
		"Q\3\2\2\2\23S\3\2\2\2\25U\3\2\2\2\27Y\3\2\2\2\31\\\3\2\2\2\33^\3\2\2\2"+
		"\35`\3\2\2\2\37b\3\2\2\2!d\3\2\2\2#f\3\2\2\2%i\3\2\2\2\'k\3\2\2\2)m\3"+
		"\2\2\2+p\3\2\2\2-\u008a\3\2\2\2/\u0094\3\2\2\2\61\u0096\3\2\2\2\63\u009c"+
		"\3\2\2\2\65\u00a9\3\2\2\2\67\u00ad\3\2\2\29\u00b2\3\2\2\2;\u00b8\3\2\2"+
		"\2=\u00bb\3\2\2\2?@\7&\2\2@\4\3\2\2\2AB\7\60\2\2BC\7\60\2\2C\6\3\2\2\2"+
		"DE\7\60\2\2EF\7,\2\2F\b\3\2\2\2GH\7]\2\2HI\7,\2\2IJ\7_\2\2J\n\3\2\2\2"+
		"KL\7,\2\2L\f\3\2\2\2MN\7]\2\2N\16\3\2\2\2OP\7_\2\2P\20\3\2\2\2QR\7.\2"+
		"\2R\22\3\2\2\2ST\7\60\2\2T\24\3\2\2\2UV\7]\2\2VW\7A\2\2WX\7*\2\2X\26\3"+
		"\2\2\2YZ\7+\2\2Z[\7_\2\2[\30\3\2\2\2\\]\7*\2\2]\32\3\2\2\2^_\7+\2\2_\34"+
		"\3\2\2\2`a\7B\2\2a\36\3\2\2\2bc\7@\2\2c \3\2\2\2de\7>\2\2e\"\3\2\2\2f"+
		"g\7?\2\2gh\7?\2\2h$\3\2\2\2ij\7<\2\2j&\3\2\2\2kl\7#\2\2l(\3\2\2\2mn\7"+
		"(\2\2no\7(\2\2o*\3\2\2\2pq\7~\2\2qr\7~\2\2r,\3\2\2\2su\7/\2\2ts\3\2\2"+
		"\2tu\3\2\2\2uv\3\2\2\2vw\5/\30\2wy\7\60\2\2xz\t\2\2\2yx\3\2\2\2z{\3\2"+
		"\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}\177\5\61\31\2~}\3\2\2\2~\177\3\2\2"+
		"\2\177\u008b\3\2\2\2\u0080\u0082\7/\2\2\u0081\u0080\3\2\2\2\u0081\u0082"+
		"\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0084\5/\30\2\u0084\u0085\5\61\31\2"+
		"\u0085\u008b\3\2\2\2\u0086\u0088\7/\2\2\u0087\u0086\3\2\2\2\u0087\u0088"+
		"\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008b\5/\30\2\u008at\3\2\2\2\u008a"+
		"\u0081\3\2\2\2\u008a\u0087\3\2\2\2\u008b.\3\2\2\2\u008c\u0095\7\62\2\2"+
		"\u008d\u0091\t\3\2\2\u008e\u0090\t\2\2\2\u008f\u008e\3\2\2\2\u0090\u0093"+
		"\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0095\3\2\2\2\u0093"+
		"\u0091\3\2\2\2\u0094\u008c\3\2\2\2\u0094\u008d\3\2\2\2\u0095\60\3\2\2"+
		"\2\u0096\u0098\t\4\2\2\u0097\u0099\t\5\2\2\u0098\u0097\3\2\2\2\u0098\u0099"+
		"\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009b\5/\30\2\u009b\62\3\2\2\2\u009c"+
		"\u00a2\7)\2\2\u009d\u00a1\n\6\2\2\u009e\u009f\7^\2\2\u009f\u00a1\13\2"+
		"\2\2\u00a0\u009d\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\u00a4\3\2\2\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a5\3\2\2\2\u00a4\u00a2\3\2"+
		"\2\2\u00a5\u00a6\7)\2\2\u00a6\64\3\2\2\2\u00a7\u00aa\5\67\34\2\u00a8\u00aa"+
		"\n\7\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab"+
		"\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\66\3\2\2\2\u00ad\u00b0\7^\2\2"+
		"\u00ae\u00b1\t\b\2\2\u00af\u00b1\59\35\2\u00b0\u00ae\3\2\2\2\u00b0\u00af"+
		"\3\2\2\2\u00b18\3\2\2\2\u00b2\u00b3\7w\2\2\u00b3\u00b4\5;\36\2\u00b4\u00b5"+
		"\5;\36\2\u00b5\u00b6\5;\36\2\u00b6\u00b7\5;\36\2\u00b7:\3\2\2\2\u00b8"+
		"\u00b9\t\t\2\2\u00b9<\3\2\2\2\u00ba\u00bc\t\n\2\2\u00bb\u00ba\3\2\2\2"+
		"\u00bc\u00bd\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00bf"+
		"\3\2\2\2\u00bf\u00c0\b\37\2\2\u00c0>\3\2\2\2\22\2t{~\u0081\u0087\u008a"+
		"\u0091\u0094\u0098\u00a0\u00a2\u00a9\u00ab\u00b0\u00bd\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}