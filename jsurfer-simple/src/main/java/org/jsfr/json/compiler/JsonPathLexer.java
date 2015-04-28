// Generated from JsonPath.g4 by ANTLR 4.5

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
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, COLON=19, KEY=20, NUM=21, WS=22;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "COLON", "KEY", "NUM", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'.'", "'['", "','", "']'", 
		"'&&'", "'||'", "'@.'", "'>'", "'<'", "'@.length-'", "'=='", "'==''", 
		"'''", "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "COLON", "KEY", "NUM", "WS"
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\30\177\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3"+
		"\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3"+
		"\22\3\23\3\23\3\24\3\24\3\25\3\25\7\25j\n\25\f\25\16\25m\13\25\3\26\3"+
		"\26\3\26\7\26r\n\26\f\26\16\26u\13\26\5\26w\n\26\3\27\6\27z\n\27\r\27"+
		"\16\27{\3\27\3\27\2\2\30\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30\3\2\7\4\2C"+
		"\\c|\6\2\62;C\\aac|\3\2\63;\3\2\62;\5\2\13\f\17\17\"\"\u0082\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\3/\3\2\2\2\5\61\3\2"+
		"\2\2\7\64\3\2\2\2\t\67\3\2\2\2\13;\3\2\2\2\r=\3\2\2\2\17?\3\2\2\2\21A"+
		"\3\2\2\2\23C\3\2\2\2\25E\3\2\2\2\27H\3\2\2\2\31K\3\2\2\2\33N\3\2\2\2\35"+
		"P\3\2\2\2\37R\3\2\2\2!\\\3\2\2\2#_\3\2\2\2%c\3\2\2\2\'e\3\2\2\2)g\3\2"+
		"\2\2+v\3\2\2\2-y\3\2\2\2/\60\7&\2\2\60\4\3\2\2\2\61\62\7\60\2\2\62\63"+
		"\7\60\2\2\63\6\3\2\2\2\64\65\7\60\2\2\65\66\7,\2\2\66\b\3\2\2\2\678\7"+
		"]\2\289\7,\2\29:\7_\2\2:\n\3\2\2\2;<\7,\2\2<\f\3\2\2\2=>\7\60\2\2>\16"+
		"\3\2\2\2?@\7]\2\2@\20\3\2\2\2AB\7.\2\2B\22\3\2\2\2CD\7_\2\2D\24\3\2\2"+
		"\2EF\7(\2\2FG\7(\2\2G\26\3\2\2\2HI\7~\2\2IJ\7~\2\2J\30\3\2\2\2KL\7B\2"+
		"\2LM\7\60\2\2M\32\3\2\2\2NO\7@\2\2O\34\3\2\2\2PQ\7>\2\2Q\36\3\2\2\2RS"+
		"\7B\2\2ST\7\60\2\2TU\7n\2\2UV\7g\2\2VW\7p\2\2WX\7i\2\2XY\7v\2\2YZ\7j\2"+
		"\2Z[\7/\2\2[ \3\2\2\2\\]\7?\2\2]^\7?\2\2^\"\3\2\2\2_`\7?\2\2`a\7?\2\2"+
		"ab\7)\2\2b$\3\2\2\2cd\7)\2\2d&\3\2\2\2ef\7<\2\2f(\3\2\2\2gk\t\2\2\2hj"+
		"\t\3\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2l*\3\2\2\2mk\3\2\2\2n"+
		"w\7\62\2\2os\t\4\2\2pr\t\5\2\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2"+
		"tw\3\2\2\2us\3\2\2\2vn\3\2\2\2vo\3\2\2\2w,\3\2\2\2xz\t\6\2\2yx\3\2\2\2"+
		"z{\3\2\2\2{y\3\2\2\2{|\3\2\2\2|}\3\2\2\2}~\b\27\2\2~.\3\2\2\2\7\2ksv{"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}