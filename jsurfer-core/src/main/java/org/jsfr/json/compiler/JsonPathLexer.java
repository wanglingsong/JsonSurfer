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
		QUOTED_STRING=23, BOOL=24, KEY=25, WS=26;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"COLON", "NegationOperator", "AndOperator", "OrOperator", "NUM", "QUOTED_STRING", 
		"BOOL", "KEY", "INT", "EXP", "ESC", "UNICODE", "HEX", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'['", "']'", "','", "'.'", 
		"'[?('", "')]'", "'('", "')'", "'@'", "'>'", "'<'", "'=='", "':'", "'!'", 
		"'&&'", "'||'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "COLON", "NegationOperator", "AndOperator", 
		"OrOperator", "NUM", "QUOTED_STRING", "BOOL", "KEY", "WS"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\34\u00ce\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\27\5\27w\n\27\3\27\3\27\3\27\6\27|\n\27\r"+
		"\27\16\27}\3\27\5\27\u0081\n\27\3\27\5\27\u0084\n\27\3\27\3\27\3\27\3"+
		"\27\5\27\u008a\n\27\3\27\5\27\u008d\n\27\3\30\3\30\3\30\3\30\7\30\u0093"+
		"\n\30\f\30\16\30\u0096\13\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\5\31\u00a3\n\31\3\32\3\32\6\32\u00a7\n\32\r\32\16\32\u00a8"+
		"\3\33\3\33\3\33\7\33\u00ae\n\33\f\33\16\33\u00b1\13\33\5\33\u00b3\n\33"+
		"\3\34\3\34\5\34\u00b7\n\34\3\34\3\34\3\35\3\35\3\35\5\35\u00be\n\35\3"+
		"\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3 \6 \u00c9\n \r \16 \u00ca\3 "+
		"\3 \2\2!\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\2\67\29\2"+
		";\2=\2?\34\3\2\13\3\2\62;\4\2))^^\f\2\13\f\17\17\"$(,..\60\60<<>B]_~~"+
		"\3\2\63;\4\2GGgg\4\2--//\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\5\2\13\f"+
		"\17\17\"\"\2\u00d9\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2?\3\2\2\2\3A\3\2\2\2\5C\3"+
		"\2\2\2\7F\3\2\2\2\tI\3\2\2\2\13M\3\2\2\2\rO\3\2\2\2\17Q\3\2\2\2\21S\3"+
		"\2\2\2\23U\3\2\2\2\25W\3\2\2\2\27[\3\2\2\2\31^\3\2\2\2\33`\3\2\2\2\35"+
		"b\3\2\2\2\37d\3\2\2\2!f\3\2\2\2#h\3\2\2\2%k\3\2\2\2\'m\3\2\2\2)o\3\2\2"+
		"\2+r\3\2\2\2-\u008c\3\2\2\2/\u008e\3\2\2\2\61\u00a2\3\2\2\2\63\u00a6\3"+
		"\2\2\2\65\u00b2\3\2\2\2\67\u00b4\3\2\2\29\u00ba\3\2\2\2;\u00bf\3\2\2\2"+
		"=\u00c5\3\2\2\2?\u00c8\3\2\2\2AB\7&\2\2B\4\3\2\2\2CD\7\60\2\2DE\7\60\2"+
		"\2E\6\3\2\2\2FG\7\60\2\2GH\7,\2\2H\b\3\2\2\2IJ\7]\2\2JK\7,\2\2KL\7_\2"+
		"\2L\n\3\2\2\2MN\7,\2\2N\f\3\2\2\2OP\7]\2\2P\16\3\2\2\2QR\7_\2\2R\20\3"+
		"\2\2\2ST\7.\2\2T\22\3\2\2\2UV\7\60\2\2V\24\3\2\2\2WX\7]\2\2XY\7A\2\2Y"+
		"Z\7*\2\2Z\26\3\2\2\2[\\\7+\2\2\\]\7_\2\2]\30\3\2\2\2^_\7*\2\2_\32\3\2"+
		"\2\2`a\7+\2\2a\34\3\2\2\2bc\7B\2\2c\36\3\2\2\2de\7@\2\2e \3\2\2\2fg\7"+
		">\2\2g\"\3\2\2\2hi\7?\2\2ij\7?\2\2j$\3\2\2\2kl\7<\2\2l&\3\2\2\2mn\7#\2"+
		"\2n(\3\2\2\2op\7(\2\2pq\7(\2\2q*\3\2\2\2rs\7~\2\2st\7~\2\2t,\3\2\2\2u"+
		"w\7/\2\2vu\3\2\2\2vw\3\2\2\2wx\3\2\2\2xy\5\65\33\2y{\7\60\2\2z|\t\2\2"+
		"\2{z\3\2\2\2|}\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\u0080\3\2\2\2\177\u0081\5"+
		"\67\34\2\u0080\177\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u008d\3\2\2\2\u0082"+
		"\u0084\7/\2\2\u0083\u0082\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0085\3\2"+
		"\2\2\u0085\u0086\5\65\33\2\u0086\u0087\5\67\34\2\u0087\u008d\3\2\2\2\u0088"+
		"\u008a\7/\2\2\u0089\u0088\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b\3\2"+
		"\2\2\u008b\u008d\5\65\33\2\u008cv\3\2\2\2\u008c\u0083\3\2\2\2\u008c\u0089"+
		"\3\2\2\2\u008d.\3\2\2\2\u008e\u0094\7)\2\2\u008f\u0093\n\3\2\2\u0090\u0091"+
		"\7^\2\2\u0091\u0093\13\2\2\2\u0092\u008f\3\2\2\2\u0092\u0090\3\2\2\2\u0093"+
		"\u0096\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0097\3\2"+
		"\2\2\u0096\u0094\3\2\2\2\u0097\u0098\7)\2\2\u0098\60\3\2\2\2\u0099\u009a"+
		"\7v\2\2\u009a\u009b\7t\2\2\u009b\u009c\7w\2\2\u009c\u00a3\7g\2\2\u009d"+
		"\u009e\7h\2\2\u009e\u009f\7c\2\2\u009f\u00a0\7n\2\2\u00a0\u00a1\7u\2\2"+
		"\u00a1\u00a3\7g\2\2\u00a2\u0099\3\2\2\2\u00a2\u009d\3\2\2\2\u00a3\62\3"+
		"\2\2\2\u00a4\u00a7\59\35\2\u00a5\u00a7\n\4\2\2\u00a6\u00a4\3\2\2\2\u00a6"+
		"\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2"+
		"\2\2\u00a9\64\3\2\2\2\u00aa\u00b3\7\62\2\2\u00ab\u00af\t\5\2\2\u00ac\u00ae"+
		"\t\2\2\2\u00ad\u00ac\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00af"+
		"\u00b0\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00aa\3\2"+
		"\2\2\u00b2\u00ab\3\2\2\2\u00b3\66\3\2\2\2\u00b4\u00b6\t\6\2\2\u00b5\u00b7"+
		"\t\7\2\2\u00b6\u00b5\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8"+
		"\u00b9\5\65\33\2\u00b98\3\2\2\2\u00ba\u00bd\7^\2\2\u00bb\u00be\t\b\2\2"+
		"\u00bc\u00be\5;\36\2\u00bd\u00bb\3\2\2\2\u00bd\u00bc\3\2\2\2\u00be:\3"+
		"\2\2\2\u00bf\u00c0\7w\2\2\u00c0\u00c1\5=\37\2\u00c1\u00c2\5=\37\2\u00c2"+
		"\u00c3\5=\37\2\u00c3\u00c4\5=\37\2\u00c4<\3\2\2\2\u00c5\u00c6\t\t\2\2"+
		"\u00c6>\3\2\2\2\u00c7\u00c9\t\n\2\2\u00c8\u00c7\3\2\2\2\u00c9\u00ca\3"+
		"\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc"+
		"\u00cd\b \2\2\u00cd@\3\2\2\2\23\2v}\u0080\u0083\u0089\u008c\u0092\u0094"+
		"\u00a2\u00a6\u00a8\u00af\u00b2\u00b6\u00bd\u00ca\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}