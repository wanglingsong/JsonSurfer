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
		T__17=18, KEY=19, NUM=20, WS=21;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "KEY", "NUM", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.'", "'['", "','", "']'", "'.*'", "'[*]'", "'*'", 
		"'&&'", "'||'", "'@.'", "'>'", "'<'", "'@.length-'", "'=='", "'==''", 
		"'''"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "KEY", "NUM", "WS"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\27{\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3"+
		"\23\3\24\3\24\7\24f\n\24\f\24\16\24i\13\24\3\25\3\25\3\25\7\25n\n\25\f"+
		"\25\16\25q\13\25\5\25s\n\25\3\26\6\26v\n\26\r\26\16\26w\3\26\3\26\2\2"+
		"\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27\3\2\7\4\2C\\c|\5\2\62;C\\c|\3\2\63"+
		";\3\2\62;\5\2\13\f\17\17\"\"~\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\3-\3\2\2\2\5/\3\2\2\2\7\62\3\2\2\2\t\64\3\2\2\2\13\66\3\2\2"+
		"\2\r8\3\2\2\2\17:\3\2\2\2\21=\3\2\2\2\23A\3\2\2\2\25C\3\2\2\2\27F\3\2"+
		"\2\2\31I\3\2\2\2\33L\3\2\2\2\35N\3\2\2\2\37P\3\2\2\2!Z\3\2\2\2#]\3\2\2"+
		"\2%a\3\2\2\2\'c\3\2\2\2)r\3\2\2\2+u\3\2\2\2-.\7&\2\2.\4\3\2\2\2/\60\7"+
		"\60\2\2\60\61\7\60\2\2\61\6\3\2\2\2\62\63\7\60\2\2\63\b\3\2\2\2\64\65"+
		"\7]\2\2\65\n\3\2\2\2\66\67\7.\2\2\67\f\3\2\2\289\7_\2\29\16\3\2\2\2:;"+
		"\7\60\2\2;<\7,\2\2<\20\3\2\2\2=>\7]\2\2>?\7,\2\2?@\7_\2\2@\22\3\2\2\2"+
		"AB\7,\2\2B\24\3\2\2\2CD\7(\2\2DE\7(\2\2E\26\3\2\2\2FG\7~\2\2GH\7~\2\2"+
		"H\30\3\2\2\2IJ\7B\2\2JK\7\60\2\2K\32\3\2\2\2LM\7@\2\2M\34\3\2\2\2NO\7"+
		">\2\2O\36\3\2\2\2PQ\7B\2\2QR\7\60\2\2RS\7n\2\2ST\7g\2\2TU\7p\2\2UV\7i"+
		"\2\2VW\7v\2\2WX\7j\2\2XY\7/\2\2Y \3\2\2\2Z[\7?\2\2[\\\7?\2\2\\\"\3\2\2"+
		"\2]^\7?\2\2^_\7?\2\2_`\7)\2\2`$\3\2\2\2ab\7)\2\2b&\3\2\2\2cg\t\2\2\2d"+
		"f\t\3\2\2ed\3\2\2\2fi\3\2\2\2ge\3\2\2\2gh\3\2\2\2h(\3\2\2\2ig\3\2\2\2"+
		"js\7\62\2\2ko\t\4\2\2ln\t\5\2\2ml\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2"+
		"\2ps\3\2\2\2qo\3\2\2\2rj\3\2\2\2rk\3\2\2\2s*\3\2\2\2tv\t\6\2\2ut\3\2\2"+
		"\2vw\3\2\2\2wu\3\2\2\2wx\3\2\2\2xy\3\2\2\2yz\b\26\2\2z,\3\2\2\2\7\2go"+
		"rw\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}