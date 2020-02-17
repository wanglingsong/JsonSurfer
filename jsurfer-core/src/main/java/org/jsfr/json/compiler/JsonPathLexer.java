// Generated from JsonPath.g4 by ANTLR 4.7.2

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
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, COLON=19, NegationOperator=20, AndOperator=21, OrOperator=22, 
		NUM=23, QUOTED_STRING=24, REGEX=25, BOOL=26, KEY=27, WS=28;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "COLON", "NegationOperator", "AndOperator", "OrOperator", "NUM", 
			"QUOTED_STRING", "REGEX", "BOOL", "KEY", "INT", "EXP", "ESC", "UNICODE", 
			"HEX", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'['", "']'", "'.'", "','", 
			"'[?('", "')]'", "'('", "')'", "'@'", "'>'", "'<'", "'=='", "'=~'", "':'", 
			"'!'", "'&&'", "'||'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "COLON", "NegationOperator", 
			"AndOperator", "OrOperator", "NUM", "QUOTED_STRING", "REGEX", "BOOL", 
			"KEY", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\36\u00f0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r"+
		"\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\5\30~\n\30"+
		"\3\30\3\30\3\30\6\30\u0083\n\30\r\30\16\30\u0084\3\30\5\30\u0088\n\30"+
		"\3\30\5\30\u008b\n\30\3\30\3\30\3\30\3\30\5\30\u0091\n\30\3\30\5\30\u0094"+
		"\n\30\3\31\3\31\3\31\3\31\7\31\u009a\n\31\f\31\16\31\u009d\13\31\3\31"+
		"\3\31\3\31\3\31\3\31\7\31\u00a4\n\31\f\31\16\31\u00a7\13\31\3\31\5\31"+
		"\u00aa\n\31\3\32\3\32\3\32\3\32\7\32\u00b0\n\32\f\32\16\32\u00b3\13\32"+
		"\3\32\3\32\7\32\u00b7\n\32\f\32\16\32\u00ba\13\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\5\33\u00c5\n\33\3\34\3\34\6\34\u00c9\n\34\r"+
		"\34\16\34\u00ca\3\35\3\35\3\35\7\35\u00d0\n\35\f\35\16\35\u00d3\13\35"+
		"\5\35\u00d5\n\35\3\36\3\36\5\36\u00d9\n\36\3\36\3\36\3\37\3\37\3\37\5"+
		"\37\u00e0\n\37\3 \3 \3 \3 \3 \3 \3!\3!\3\"\6\"\u00eb\n\"\r\"\16\"\u00ec"+
		"\3\"\3\"\2\2#\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\2;\2=\2?\2A\2C\36\3\2\16\3\2\62;\4\2))^^\4\2$$^^\4\2\61\61^^\t"+
		"\2WWffkkoouuwwzz\f\2\13\f\17\17\"$(,..\60\60<<>B]_~~\3\2\63;\4\2GGgg\4"+
		"\2--//\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\5\2\13\f\17\17\"\"\2\u0101"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2C\3\2\2\2\3E\3"+
		"\2\2\2\5G\3\2\2\2\7J\3\2\2\2\tM\3\2\2\2\13Q\3\2\2\2\rS\3\2\2\2\17U\3\2"+
		"\2\2\21W\3\2\2\2\23Y\3\2\2\2\25[\3\2\2\2\27_\3\2\2\2\31b\3\2\2\2\33d\3"+
		"\2\2\2\35f\3\2\2\2\37h\3\2\2\2!j\3\2\2\2#l\3\2\2\2%o\3\2\2\2\'r\3\2\2"+
		"\2)t\3\2\2\2+v\3\2\2\2-y\3\2\2\2/\u0093\3\2\2\2\61\u00a9\3\2\2\2\63\u00ab"+
		"\3\2\2\2\65\u00c4\3\2\2\2\67\u00c8\3\2\2\29\u00d4\3\2\2\2;\u00d6\3\2\2"+
		"\2=\u00dc\3\2\2\2?\u00e1\3\2\2\2A\u00e7\3\2\2\2C\u00ea\3\2\2\2EF\7&\2"+
		"\2F\4\3\2\2\2GH\7\60\2\2HI\7\60\2\2I\6\3\2\2\2JK\7\60\2\2KL\7,\2\2L\b"+
		"\3\2\2\2MN\7]\2\2NO\7,\2\2OP\7_\2\2P\n\3\2\2\2QR\7,\2\2R\f\3\2\2\2ST\7"+
		"]\2\2T\16\3\2\2\2UV\7_\2\2V\20\3\2\2\2WX\7\60\2\2X\22\3\2\2\2YZ\7.\2\2"+
		"Z\24\3\2\2\2[\\\7]\2\2\\]\7A\2\2]^\7*\2\2^\26\3\2\2\2_`\7+\2\2`a\7_\2"+
		"\2a\30\3\2\2\2bc\7*\2\2c\32\3\2\2\2de\7+\2\2e\34\3\2\2\2fg\7B\2\2g\36"+
		"\3\2\2\2hi\7@\2\2i \3\2\2\2jk\7>\2\2k\"\3\2\2\2lm\7?\2\2mn\7?\2\2n$\3"+
		"\2\2\2op\7?\2\2pq\7\u0080\2\2q&\3\2\2\2rs\7<\2\2s(\3\2\2\2tu\7#\2\2u*"+
		"\3\2\2\2vw\7(\2\2wx\7(\2\2x,\3\2\2\2yz\7~\2\2z{\7~\2\2{.\3\2\2\2|~\7/"+
		"\2\2}|\3\2\2\2}~\3\2\2\2~\177\3\2\2\2\177\u0080\59\35\2\u0080\u0082\7"+
		"\60\2\2\u0081\u0083\t\2\2\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084"+
		"\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2\2\2\u0086\u0088\5;"+
		"\36\2\u0087\u0086\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u0094\3\2\2\2\u0089"+
		"\u008b\7/\2\2\u008a\u0089\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008c\3\2"+
		"\2\2\u008c\u008d\59\35\2\u008d\u008e\5;\36\2\u008e\u0094\3\2\2\2\u008f"+
		"\u0091\7/\2\2\u0090\u008f\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0092\3\2"+
		"\2\2\u0092\u0094\59\35\2\u0093}\3\2\2\2\u0093\u008a\3\2\2\2\u0093\u0090"+
		"\3\2\2\2\u0094\60\3\2\2\2\u0095\u009b\7)\2\2\u0096\u009a\n\3\2\2\u0097"+
		"\u0098\7^\2\2\u0098\u009a\13\2\2\2\u0099\u0096\3\2\2\2\u0099\u0097\3\2"+
		"\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c"+
		"\u009e\3\2\2\2\u009d\u009b\3\2\2\2\u009e\u00aa\7)\2\2\u009f\u00a5\7$\2"+
		"\2\u00a0\u00a4\n\4\2\2\u00a1\u00a2\7^\2\2\u00a2\u00a4\13\2\2\2\u00a3\u00a0"+
		"\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00aa\7$"+
		"\2\2\u00a9\u0095\3\2\2\2\u00a9\u009f\3\2\2\2\u00aa\62\3\2\2\2\u00ab\u00b1"+
		"\7\61\2\2\u00ac\u00b0\n\5\2\2\u00ad\u00ae\7^\2\2\u00ae\u00b0\13\2\2\2"+
		"\u00af\u00ac\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af"+
		"\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b4\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4"+
		"\u00b8\7\61\2\2\u00b5\u00b7\t\6\2\2\u00b6\u00b5\3\2\2\2\u00b7\u00ba\3"+
		"\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\64\3\2\2\2\u00ba"+
		"\u00b8\3\2\2\2\u00bb\u00bc\7v\2\2\u00bc\u00bd\7t\2\2\u00bd\u00be\7w\2"+
		"\2\u00be\u00c5\7g\2\2\u00bf\u00c0\7h\2\2\u00c0\u00c1\7c\2\2\u00c1\u00c2"+
		"\7n\2\2\u00c2\u00c3\7u\2\2\u00c3\u00c5\7g\2\2\u00c4\u00bb\3\2\2\2\u00c4"+
		"\u00bf\3\2\2\2\u00c5\66\3\2\2\2\u00c6\u00c9\5=\37\2\u00c7\u00c9\n\7\2"+
		"\2\u00c8\u00c6\3\2\2\2\u00c8\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00c8"+
		"\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb8\3\2\2\2\u00cc\u00d5\7\62\2\2\u00cd"+
		"\u00d1\t\b\2\2\u00ce\u00d0\t\2\2\2\u00cf\u00ce\3\2\2\2\u00d0\u00d3\3\2"+
		"\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3"+
		"\u00d1\3\2\2\2\u00d4\u00cc\3\2\2\2\u00d4\u00cd\3\2\2\2\u00d5:\3\2\2\2"+
		"\u00d6\u00d8\t\t\2\2\u00d7\u00d9\t\n\2\2\u00d8\u00d7\3\2\2\2\u00d8\u00d9"+
		"\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\u00db\59\35\2\u00db<\3\2\2\2\u00dc"+
		"\u00df\7^\2\2\u00dd\u00e0\t\13\2\2\u00de\u00e0\5? \2\u00df\u00dd\3\2\2"+
		"\2\u00df\u00de\3\2\2\2\u00e0>\3\2\2\2\u00e1\u00e2\7w\2\2\u00e2\u00e3\5"+
		"A!\2\u00e3\u00e4\5A!\2\u00e4\u00e5\5A!\2\u00e5\u00e6\5A!\2\u00e6@\3\2"+
		"\2\2\u00e7\u00e8\t\f\2\2\u00e8B\3\2\2\2\u00e9\u00eb\t\r\2\2\u00ea\u00e9"+
		"\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed"+
		"\u00ee\3\2\2\2\u00ee\u00ef\b\"\2\2\u00efD\3\2\2\2\31\2}\u0084\u0087\u008a"+
		"\u0090\u0093\u0099\u009b\u00a3\u00a5\u00a9\u00af\u00b1\u00b8\u00c4\u00c8"+
		"\u00ca\u00d1\u00d4\u00d8\u00df\u00ec\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}