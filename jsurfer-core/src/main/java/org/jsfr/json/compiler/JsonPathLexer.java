/*
 * The MIT License
 *
 * Copyright (c) 2017 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

// Generated from JsonPath.g4 by ANTLR 4.5.3

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
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, COLON=19, NUM=20, KEY=21, WS=22;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "COLON", "NUM", "INT", "EXP", "KEY", "ESC", "UNICODE", "HEX", 
		"WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'['", "']'", "','", "'.'", 
		"'&&'", "'||'", "'@.'", "'>'", "'<'", "'@.length-'", "'=='", "'==''", 
		"'''", "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "COLON", "NUM", "KEY", "WS"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\30\u00b4\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23"+
		"\3\24\3\24\3\25\5\25s\n\25\3\25\3\25\3\25\6\25x\n\25\r\25\16\25y\3\25"+
		"\5\25}\n\25\3\25\5\25\u0080\n\25\3\25\3\25\3\25\3\25\5\25\u0086\n\25\3"+
		"\25\5\25\u0089\n\25\3\26\3\26\3\26\7\26\u008e\n\26\f\26\16\26\u0091\13"+
		"\26\5\26\u0093\n\26\3\27\3\27\5\27\u0097\n\27\3\27\3\27\3\30\3\30\6\30"+
		"\u009d\n\30\r\30\16\30\u009e\3\31\3\31\3\31\5\31\u00a4\n\31\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\33\3\33\3\34\6\34\u00af\n\34\r\34\16\34\u00b0\3"+
		"\34\3\34\2\2\35\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\2-\2/\27\61\2\63\2\65\2\67"+
		"\30\3\2\n\3\2\62;\3\2\63;\4\2GGgg\4\2--//\13\2\13\f\17\17\"\"$$,,..\60"+
		"\60<<]_\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\5\2\13\f\17\17\"\"\u00bc\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2"+
		"\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2/\3\2\2\2\2\67\3\2\2\2\39\3\2\2"+
		"\2\5;\3\2\2\2\7>\3\2\2\2\tA\3\2\2\2\13E\3\2\2\2\rG\3\2\2\2\17I\3\2\2\2"+
		"\21K\3\2\2\2\23M\3\2\2\2\25O\3\2\2\2\27R\3\2\2\2\31U\3\2\2\2\33X\3\2\2"+
		"\2\35Z\3\2\2\2\37\\\3\2\2\2!f\3\2\2\2#i\3\2\2\2%m\3\2\2\2\'o\3\2\2\2)"+
		"\u0088\3\2\2\2+\u0092\3\2\2\2-\u0094\3\2\2\2/\u009c\3\2\2\2\61\u00a0\3"+
		"\2\2\2\63\u00a5\3\2\2\2\65\u00ab\3\2\2\2\67\u00ae\3\2\2\29:\7&\2\2:\4"+
		"\3\2\2\2;<\7\60\2\2<=\7\60\2\2=\6\3\2\2\2>?\7\60\2\2?@\7,\2\2@\b\3\2\2"+
		"\2AB\7]\2\2BC\7,\2\2CD\7_\2\2D\n\3\2\2\2EF\7,\2\2F\f\3\2\2\2GH\7]\2\2"+
		"H\16\3\2\2\2IJ\7_\2\2J\20\3\2\2\2KL\7.\2\2L\22\3\2\2\2MN\7\60\2\2N\24"+
		"\3\2\2\2OP\7(\2\2PQ\7(\2\2Q\26\3\2\2\2RS\7~\2\2ST\7~\2\2T\30\3\2\2\2U"+
		"V\7B\2\2VW\7\60\2\2W\32\3\2\2\2XY\7@\2\2Y\34\3\2\2\2Z[\7>\2\2[\36\3\2"+
		"\2\2\\]\7B\2\2]^\7\60\2\2^_\7n\2\2_`\7g\2\2`a\7p\2\2ab\7i\2\2bc\7v\2\2"+
		"cd\7j\2\2de\7/\2\2e \3\2\2\2fg\7?\2\2gh\7?\2\2h\"\3\2\2\2ij\7?\2\2jk\7"+
		"?\2\2kl\7)\2\2l$\3\2\2\2mn\7)\2\2n&\3\2\2\2op\7<\2\2p(\3\2\2\2qs\7/\2"+
		"\2rq\3\2\2\2rs\3\2\2\2st\3\2\2\2tu\5+\26\2uw\7\60\2\2vx\t\2\2\2wv\3\2"+
		"\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z|\3\2\2\2{}\5-\27\2|{\3\2\2\2|}\3\2"+
		"\2\2}\u0089\3\2\2\2~\u0080\7/\2\2\177~\3\2\2\2\177\u0080\3\2\2\2\u0080"+
		"\u0081\3\2\2\2\u0081\u0082\5+\26\2\u0082\u0083\5-\27\2\u0083\u0089\3\2"+
		"\2\2\u0084\u0086\7/\2\2\u0085\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086"+
		"\u0087\3\2\2\2\u0087\u0089\5+\26\2\u0088r\3\2\2\2\u0088\177\3\2\2\2\u0088"+
		"\u0085\3\2\2\2\u0089*\3\2\2\2\u008a\u0093\7\62\2\2\u008b\u008f\t\3\2\2"+
		"\u008c\u008e\t\2\2\2\u008d\u008c\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d"+
		"\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f\3\2\2\2\u0092"+
		"\u008a\3\2\2\2\u0092\u008b\3\2\2\2\u0093,\3\2\2\2\u0094\u0096\t\4\2\2"+
		"\u0095\u0097\t\5\2\2\u0096\u0095\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098"+
		"\3\2\2\2\u0098\u0099\5+\26\2\u0099.\3\2\2\2\u009a\u009d\5\61\31\2\u009b"+
		"\u009d\n\6\2\2\u009c\u009a\3\2\2\2\u009c\u009b\3\2\2\2\u009d\u009e\3\2"+
		"\2\2\u009e\u009c\3\2\2\2\u009e\u009f\3\2\2\2\u009f\60\3\2\2\2\u00a0\u00a3"+
		"\7^\2\2\u00a1\u00a4\t\7\2\2\u00a2\u00a4\5\63\32\2\u00a3\u00a1\3\2\2\2"+
		"\u00a3\u00a2\3\2\2\2\u00a4\62\3\2\2\2\u00a5\u00a6\7w\2\2\u00a6\u00a7\5"+
		"\65\33\2\u00a7\u00a8\5\65\33\2\u00a8\u00a9\5\65\33\2\u00a9\u00aa\5\65"+
		"\33\2\u00aa\64\3\2\2\2\u00ab\u00ac\t\b\2\2\u00ac\66\3\2\2\2\u00ad\u00af"+
		"\t\t\2\2\u00ae\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0"+
		"\u00b1\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\b\34\2\2\u00b38\3\2\2\2"+
		"\20\2ry|\177\u0085\u0088\u008f\u0092\u0096\u009c\u009e\u00a3\u00b0\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}