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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonPathParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, COLON=19, NUM=20, KEY=21, WS=22;
	public static final int
		RULE_path = 0, RULE_searchChild = 1, RULE_search = 2, RULE_anyChild = 3, 
		RULE_anyIndex = 4, RULE_any = 5, RULE_index = 6, RULE_indexes = 7, RULE_slicing = 8, 
		RULE_childNode = 9, RULE_childrenNode = 10, RULE_filter = 11, RULE_expr = 12;
	public static final String[] ruleNames = {
		"path", "searchChild", "search", "anyChild", "anyIndex", "any", "index", 
		"indexes", "slicing", "childNode", "childrenNode", "filter", "expr"
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

	@Override
	public String getGrammarFileName() { return "JsonPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JsonPathParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PathContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(JsonPathParser.EOF, 0); }
		public List<SearchChildContext> searchChild() {
			return getRuleContexts(SearchChildContext.class);
		}
		public SearchChildContext searchChild(int i) {
			return getRuleContext(SearchChildContext.class,i);
		}
		public List<SearchContext> search() {
			return getRuleContexts(SearchContext.class);
		}
		public SearchContext search(int i) {
			return getRuleContext(SearchContext.class,i);
		}
		public List<IndexContext> index() {
			return getRuleContexts(IndexContext.class);
		}
		public IndexContext index(int i) {
			return getRuleContext(IndexContext.class,i);
		}
		public List<IndexesContext> indexes() {
			return getRuleContexts(IndexesContext.class);
		}
		public IndexesContext indexes(int i) {
			return getRuleContext(IndexesContext.class,i);
		}
		public List<SlicingContext> slicing() {
			return getRuleContexts(SlicingContext.class);
		}
		public SlicingContext slicing(int i) {
			return getRuleContext(SlicingContext.class,i);
		}
		public List<ChildNodeContext> childNode() {
			return getRuleContexts(ChildNodeContext.class);
		}
		public ChildNodeContext childNode(int i) {
			return getRuleContext(ChildNodeContext.class,i);
		}
		public List<ChildrenNodeContext> childrenNode() {
			return getRuleContexts(ChildrenNodeContext.class);
		}
		public ChildrenNodeContext childrenNode(int i) {
			return getRuleContext(ChildrenNodeContext.class,i);
		}
		public List<FilterContext> filter() {
			return getRuleContexts(FilterContext.class);
		}
		public FilterContext filter(int i) {
			return getRuleContext(FilterContext.class,i);
		}
		public List<AnyChildContext> anyChild() {
			return getRuleContexts(AnyChildContext.class);
		}
		public AnyChildContext anyChild(int i) {
			return getRuleContext(AnyChildContext.class,i);
		}
		public List<AnyIndexContext> anyIndex() {
			return getRuleContexts(AnyIndexContext.class);
		}
		public AnyIndexContext anyIndex(int i) {
			return getRuleContext(AnyIndexContext.class,i);
		}
		public List<AnyContext> any() {
			return getRuleContexts(AnyContext.class);
		}
		public AnyContext any(int i) {
			return getRuleContext(AnyContext.class,i);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_path);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			match(T__0);
			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__8))) != 0)) {
				{
				setState(38);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(27);
					searchChild();
					}
					break;
				case 2:
					{
					setState(28);
					search();
					}
					break;
				case 3:
					{
					setState(29);
					index();
					}
					break;
				case 4:
					{
					setState(30);
					indexes();
					}
					break;
				case 5:
					{
					setState(31);
					slicing();
					}
					break;
				case 6:
					{
					setState(32);
					childNode();
					}
					break;
				case 7:
					{
					setState(33);
					childrenNode();
					}
					break;
				case 8:
					{
					setState(34);
					filter();
					}
					break;
				case 9:
					{
					setState(35);
					anyChild();
					}
					break;
				case 10:
					{
					setState(36);
					anyIndex();
					}
					break;
				case 11:
					{
					setState(37);
					any();
					}
					break;
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(43);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SearchChildContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(JsonPathParser.KEY, 0); }
		public SearchChildContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_searchChild; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitSearchChild(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchChildContext searchChild() throws RecognitionException {
		SearchChildContext _localctx = new SearchChildContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_searchChild);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(T__1);
			setState(46);
			match(KEY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SearchContext extends ParserRuleContext {
		public SearchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_search; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitSearch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchContext search() throws RecognitionException {
		SearchContext _localctx = new SearchContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_search);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnyChildContext extends ParserRuleContext {
		public AnyChildContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyChild; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitAnyChild(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyChildContext anyChild() throws RecognitionException {
		AnyChildContext _localctx = new AnyChildContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_anyChild);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnyIndexContext extends ParserRuleContext {
		public AnyIndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyIndex; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitAnyIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyIndexContext anyIndex() throws RecognitionException {
		AnyIndexContext _localctx = new AnyIndexContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_anyIndex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnyContext extends ParserRuleContext {
		public AnyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_any; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitAny(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyContext any() throws RecognitionException {
		AnyContext _localctx = new AnyContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_any);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexContext extends ParserRuleContext {
		public TerminalNode NUM() { return getToken(JsonPathParser.NUM, 0); }
		public IndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexContext index() throws RecognitionException {
		IndexContext _localctx = new IndexContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(T__5);
			setState(57);
			match(NUM);
			setState(58);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexesContext extends ParserRuleContext {
		public List<TerminalNode> NUM() { return getTokens(JsonPathParser.NUM); }
		public TerminalNode NUM(int i) {
			return getToken(JsonPathParser.NUM, i);
		}
		public IndexesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexes; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitIndexes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexesContext indexes() throws RecognitionException {
		IndexesContext _localctx = new IndexesContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_indexes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(T__5);
			setState(61);
			match(NUM);
			setState(66);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(62);
				match(T__7);
				setState(63);
				match(NUM);
				}
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(69);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SlicingContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(JsonPathParser.COLON, 0); }
		public List<TerminalNode> NUM() { return getTokens(JsonPathParser.NUM); }
		public TerminalNode NUM(int i) {
			return getToken(JsonPathParser.NUM, i);
		}
		public SlicingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slicing; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitSlicing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SlicingContext slicing() throws RecognitionException {
		SlicingContext _localctx = new SlicingContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_slicing);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(T__5);
			setState(73);
			_la = _input.LA(1);
			if (_la==NUM) {
				{
				setState(72);
				match(NUM);
				}
			}

			setState(75);
			match(COLON);
			setState(77);
			_la = _input.LA(1);
			if (_la==NUM) {
				{
				setState(76);
				match(NUM);
				}
			}

			setState(79);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChildNodeContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(JsonPathParser.KEY, 0); }
		public ChildNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_childNode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitChildNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChildNodeContext childNode() throws RecognitionException {
		ChildNodeContext _localctx = new ChildNodeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_childNode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__8);
			setState(82);
			match(KEY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChildrenNodeContext extends ParserRuleContext {
		public List<TerminalNode> KEY() { return getTokens(JsonPathParser.KEY); }
		public TerminalNode KEY(int i) {
			return getToken(JsonPathParser.KEY, i);
		}
		public ChildrenNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_childrenNode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitChildrenNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChildrenNodeContext childrenNode() throws RecognitionException {
		ChildrenNodeContext _localctx = new ChildrenNodeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_childrenNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(T__5);
			setState(85);
			match(KEY);
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(86);
				match(T__7);
				setState(87);
				match(KEY);
				}
				}
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(93);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(T__5);
			setState(96);
			expr(0);
			setState(97);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public List<TerminalNode> KEY() { return getTokens(JsonPathParser.KEY); }
		public TerminalNode KEY(int i) {
			return getToken(JsonPathParser.KEY, i);
		}
		public TerminalNode NUM() { return getToken(JsonPathParser.NUM, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(100);
				match(T__4);
				}
				break;
			case 2:
				{
				setState(101);
				match(T__11);
				setState(102);
				match(KEY);
				}
				break;
			case 3:
				{
				setState(103);
				match(T__11);
				setState(104);
				match(KEY);
				setState(105);
				match(T__12);
				setState(106);
				match(NUM);
				}
				break;
			case 4:
				{
				setState(107);
				match(T__11);
				setState(108);
				match(KEY);
				setState(109);
				match(T__13);
				setState(110);
				match(NUM);
				}
				break;
			case 5:
				{
				setState(111);
				match(T__14);
				setState(112);
				match(NUM);
				}
				break;
			case 6:
				{
				setState(113);
				match(T__11);
				setState(114);
				match(KEY);
				setState(115);
				match(T__15);
				setState(116);
				match(NUM);
				}
				break;
			case 7:
				{
				setState(117);
				match(T__11);
				setState(118);
				match(KEY);
				setState(119);
				match(T__16);
				setState(120);
				match(KEY);
				setState(121);
				match(T__17);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(140);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(138);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(124);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(127); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(125);
								match(T__9);
								setState(126);
								expr(0);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(129); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(131);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(134); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(132);
								match(T__10);
								setState(133);
								expr(0);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(136); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					}
					} 
				}
				setState(142);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 9);
		case 1:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\30\u0092\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\7\2)\n\2\f\2\16\2,\13\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5"+
		"\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\7\tC\n\t\f\t\16\tF\13"+
		"\t\3\t\3\t\3\n\3\n\5\nL\n\n\3\n\3\n\5\nP\n\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\7\f[\n\f\f\f\16\f^\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16}\n\16\3\16\3\16\3\16\6\16"+
		"\u0082\n\16\r\16\16\16\u0083\3\16\3\16\3\16\6\16\u0089\n\16\r\16\16\16"+
		"\u008a\7\16\u008d\n\16\f\16\16\16\u0090\13\16\3\16\2\3\32\17\2\4\6\b\n"+
		"\f\16\20\22\24\26\30\32\2\2\u009d\2\34\3\2\2\2\4/\3\2\2\2\6\62\3\2\2\2"+
		"\b\64\3\2\2\2\n\66\3\2\2\2\f8\3\2\2\2\16:\3\2\2\2\20>\3\2\2\2\22I\3\2"+
		"\2\2\24S\3\2\2\2\26V\3\2\2\2\30a\3\2\2\2\32|\3\2\2\2\34*\7\3\2\2\35)\5"+
		"\4\3\2\36)\5\6\4\2\37)\5\16\b\2 )\5\20\t\2!)\5\22\n\2\")\5\24\13\2#)\5"+
		"\26\f\2$)\5\30\r\2%)\5\b\5\2&)\5\n\6\2\')\5\f\7\2(\35\3\2\2\2(\36\3\2"+
		"\2\2(\37\3\2\2\2( \3\2\2\2(!\3\2\2\2(\"\3\2\2\2(#\3\2\2\2($\3\2\2\2(%"+
		"\3\2\2\2(&\3\2\2\2(\'\3\2\2\2),\3\2\2\2*(\3\2\2\2*+\3\2\2\2+-\3\2\2\2"+
		",*\3\2\2\2-.\7\2\2\3.\3\3\2\2\2/\60\7\4\2\2\60\61\7\27\2\2\61\5\3\2\2"+
		"\2\62\63\7\4\2\2\63\7\3\2\2\2\64\65\7\5\2\2\65\t\3\2\2\2\66\67\7\6\2\2"+
		"\67\13\3\2\2\289\7\7\2\29\r\3\2\2\2:;\7\b\2\2;<\7\26\2\2<=\7\t\2\2=\17"+
		"\3\2\2\2>?\7\b\2\2?D\7\26\2\2@A\7\n\2\2AC\7\26\2\2B@\3\2\2\2CF\3\2\2\2"+
		"DB\3\2\2\2DE\3\2\2\2EG\3\2\2\2FD\3\2\2\2GH\7\t\2\2H\21\3\2\2\2IK\7\b\2"+
		"\2JL\7\26\2\2KJ\3\2\2\2KL\3\2\2\2LM\3\2\2\2MO\7\25\2\2NP\7\26\2\2ON\3"+
		"\2\2\2OP\3\2\2\2PQ\3\2\2\2QR\7\t\2\2R\23\3\2\2\2ST\7\13\2\2TU\7\27\2\2"+
		"U\25\3\2\2\2VW\7\b\2\2W\\\7\27\2\2XY\7\n\2\2Y[\7\27\2\2ZX\3\2\2\2[^\3"+
		"\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^\\\3\2\2\2_`\7\t\2\2`\27\3\2\2"+
		"\2ab\7\b\2\2bc\5\32\16\2cd\7\t\2\2d\31\3\2\2\2ef\b\16\1\2f}\7\7\2\2gh"+
		"\7\16\2\2h}\7\27\2\2ij\7\16\2\2jk\7\27\2\2kl\7\17\2\2l}\7\26\2\2mn\7\16"+
		"\2\2no\7\27\2\2op\7\20\2\2p}\7\26\2\2qr\7\21\2\2r}\7\26\2\2st\7\16\2\2"+
		"tu\7\27\2\2uv\7\22\2\2v}\7\26\2\2wx\7\16\2\2xy\7\27\2\2yz\7\23\2\2z{\7"+
		"\27\2\2{}\7\24\2\2|e\3\2\2\2|g\3\2\2\2|i\3\2\2\2|m\3\2\2\2|q\3\2\2\2|"+
		"s\3\2\2\2|w\3\2\2\2}\u008e\3\2\2\2~\u0081\f\13\2\2\177\u0080\7\f\2\2\u0080"+
		"\u0082\5\32\16\2\u0081\177\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0081\3\2"+
		"\2\2\u0083\u0084\3\2\2\2\u0084\u008d\3\2\2\2\u0085\u0088\f\n\2\2\u0086"+
		"\u0087\7\r\2\2\u0087\u0089\5\32\16\2\u0088\u0086\3\2\2\2\u0089\u008a\3"+
		"\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008d\3\2\2\2\u008c"+
		"~\3\2\2\2\u008c\u0085\3\2\2\2\u008d\u0090\3\2\2\2\u008e\u008c\3\2\2\2"+
		"\u008e\u008f\3\2\2\2\u008f\33\3\2\2\2\u0090\u008e\3\2\2\2\r(*DKO\\|\u0083"+
		"\u008a\u008c\u008e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}