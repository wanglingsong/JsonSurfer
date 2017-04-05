// Generated from JsonPath.g4 by ANTLR 4.7

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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		COLON=18, AndOperator=19, OrOperator=20, NUM=21, KEY=22, WS=23;
	public static final int
		RULE_path = 0, RULE_searchChild = 1, RULE_search = 2, RULE_anyChild = 3, 
		RULE_anyIndex = 4, RULE_any = 5, RULE_index = 6, RULE_indexes = 7, RULE_slicing = 8, 
		RULE_childNode = 9, RULE_childrenNode = 10, RULE_filter = 11, RULE_expr = 12, 
		RULE_exprExist = 13, RULE_exprGtNum = 14, RULE_exprLtNum = 15, RULE_exprEqualNum = 16, 
		RULE_exprEqualStr = 17;
	public static final String[] ruleNames = {
		"path", "searchChild", "search", "anyChild", "anyIndex", "any", "index", 
		"indexes", "slicing", "childNode", "childrenNode", "filter", "expr", "exprExist", 
		"exprGtNum", "exprLtNum", "exprEqualNum", "exprEqualStr"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'..'", "'.*'", "'[*]'", "'*'", "'['", "']'", "','", "'.'", 
		"'[?('", "')]'", "'@.'", "'>'", "'<'", "'=='", "'==''", "'''", "':'", 
		"'&&'", "'||'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "COLON", "AndOperator", "OrOperator", 
		"NUM", "KEY", "WS"
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
		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
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
			setState(36);
			match(T__0);
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__8))) != 0)) {
				{
				setState(47);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(37);
					searchChild();
					}
					break;
				case 2:
					{
					setState(38);
					search();
					}
					break;
				case 3:
					{
					setState(39);
					index();
					}
					break;
				case 4:
					{
					setState(40);
					indexes();
					}
					break;
				case 5:
					{
					setState(41);
					slicing();
					}
					break;
				case 6:
					{
					setState(42);
					childNode();
					}
					break;
				case 7:
					{
					setState(43);
					childrenNode();
					}
					break;
				case 8:
					{
					setState(44);
					anyChild();
					}
					break;
				case 9:
					{
					setState(45);
					anyIndex();
					}
					break;
				case 10:
					{
					setState(46);
					any();
					}
					break;
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(52);
				filter();
				}
			}

			setState(55);
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
			setState(57);
			match(T__1);
			setState(58);
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
			setState(60);
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
			setState(62);
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
			setState(64);
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
			setState(66);
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
			setState(68);
			match(T__5);
			setState(69);
			match(NUM);
			setState(70);
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
			setState(72);
			match(T__5);
			setState(73);
			match(NUM);
			setState(78);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(74);
				match(T__7);
				setState(75);
				match(NUM);
				}
				}
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(81);
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
			setState(83);
			match(T__5);
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NUM) {
				{
				setState(84);
				match(NUM);
				}
			}

			setState(87);
			match(COLON);
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NUM) {
				{
				setState(88);
				match(NUM);
				}
			}

			setState(91);
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
			setState(93);
			match(T__8);
			setState(94);
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
			setState(96);
			match(T__5);
			setState(97);
			match(KEY);
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(98);
				match(T__7);
				setState(99);
				match(KEY);
				}
				}
				setState(104);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(105);
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
			setState(107);
			match(T__9);
			setState(108);
			expr(0);
			setState(109);
			match(T__10);
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
		public ExprEqualNumContext exprEqualNum() {
			return getRuleContext(ExprEqualNumContext.class,0);
		}
		public ExprEqualStrContext exprEqualStr() {
			return getRuleContext(ExprEqualStrContext.class,0);
		}
		public ExprGtNumContext exprGtNum() {
			return getRuleContext(ExprGtNumContext.class,0);
		}
		public ExprLtNumContext exprLtNum() {
			return getRuleContext(ExprLtNumContext.class,0);
		}
		public ExprExistContext exprExist() {
			return getRuleContext(ExprExistContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AndOperator() { return getToken(JsonPathParser.AndOperator, 0); }
		public TerminalNode OrOperator() { return getToken(JsonPathParser.OrOperator, 0); }
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
			setState(117);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(112);
				exprEqualNum();
				}
				break;
			case 2:
				{
				setState(113);
				exprEqualStr();
				}
				break;
			case 3:
				{
				setState(114);
				exprGtNum();
				}
				break;
			case 4:
				{
				setState(115);
				exprLtNum();
				}
				break;
			case 5:
				{
				setState(116);
				exprExist();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(127);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(125);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(119);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(120);
						match(AndOperator);
						setState(121);
						expr(8);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(122);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(123);
						match(OrOperator);
						setState(124);
						expr(7);
						}
						break;
					}
					} 
				}
				setState(129);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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

	public static class ExprExistContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(JsonPathParser.KEY, 0); }
		public ExprExistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprExist; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitExprExist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprExistContext exprExist() throws RecognitionException {
		ExprExistContext _localctx = new ExprExistContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_exprExist);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(T__11);
			setState(131);
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

	public static class ExprGtNumContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(JsonPathParser.KEY, 0); }
		public TerminalNode NUM() { return getToken(JsonPathParser.NUM, 0); }
		public ExprGtNumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprGtNum; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitExprGtNum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprGtNumContext exprGtNum() throws RecognitionException {
		ExprGtNumContext _localctx = new ExprGtNumContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_exprGtNum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__11);
			setState(134);
			match(KEY);
			setState(135);
			match(T__12);
			setState(136);
			match(NUM);
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

	public static class ExprLtNumContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(JsonPathParser.KEY, 0); }
		public TerminalNode NUM() { return getToken(JsonPathParser.NUM, 0); }
		public ExprLtNumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprLtNum; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitExprLtNum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprLtNumContext exprLtNum() throws RecognitionException {
		ExprLtNumContext _localctx = new ExprLtNumContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_exprLtNum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(T__11);
			setState(139);
			match(KEY);
			setState(140);
			match(T__13);
			setState(141);
			match(NUM);
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

	public static class ExprEqualNumContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(JsonPathParser.KEY, 0); }
		public TerminalNode NUM() { return getToken(JsonPathParser.NUM, 0); }
		public ExprEqualNumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprEqualNum; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitExprEqualNum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprEqualNumContext exprEqualNum() throws RecognitionException {
		ExprEqualNumContext _localctx = new ExprEqualNumContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_exprEqualNum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__11);
			setState(144);
			match(KEY);
			setState(145);
			match(T__14);
			setState(146);
			match(NUM);
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

	public static class ExprEqualStrContext extends ParserRuleContext {
		public List<TerminalNode> KEY() { return getTokens(JsonPathParser.KEY); }
		public TerminalNode KEY(int i) {
			return getToken(JsonPathParser.KEY, i);
		}
		public ExprEqualStrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprEqualStr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPathVisitor ) return ((JsonPathVisitor<? extends T>)visitor).visitExprEqualStr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprEqualStrContext exprEqualStr() throws RecognitionException {
		ExprEqualStrContext _localctx = new ExprEqualStrContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_exprEqualStr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(T__11);
			setState(149);
			match(KEY);
			setState(150);
			match(T__15);
			setState(151);
			match(KEY);
			setState(152);
			match(T__16);
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
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31\u009d\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\62\n\2\f\2"+
		"\16\2\65\13\2\3\2\5\28\n\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\7\tO\n\t\f\t\16\tR\13\t\3\t\3"+
		"\t\3\n\3\n\5\nX\n\n\3\n\3\n\5\n\\\n\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3"+
		"\f\3\f\7\fg\n\f\f\f\16\fj\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\5\16x\n\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u0080\n"+
		"\16\f\16\16\16\u0083\13\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\2\3\32\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\2\2"+
		"\u009f\2&\3\2\2\2\4;\3\2\2\2\6>\3\2\2\2\b@\3\2\2\2\nB\3\2\2\2\fD\3\2\2"+
		"\2\16F\3\2\2\2\20J\3\2\2\2\22U\3\2\2\2\24_\3\2\2\2\26b\3\2\2\2\30m\3\2"+
		"\2\2\32w\3\2\2\2\34\u0084\3\2\2\2\36\u0087\3\2\2\2 \u008c\3\2\2\2\"\u0091"+
		"\3\2\2\2$\u0096\3\2\2\2&\63\7\3\2\2\'\62\5\4\3\2(\62\5\6\4\2)\62\5\16"+
		"\b\2*\62\5\20\t\2+\62\5\22\n\2,\62\5\24\13\2-\62\5\26\f\2.\62\5\b\5\2"+
		"/\62\5\n\6\2\60\62\5\f\7\2\61\'\3\2\2\2\61(\3\2\2\2\61)\3\2\2\2\61*\3"+
		"\2\2\2\61+\3\2\2\2\61,\3\2\2\2\61-\3\2\2\2\61.\3\2\2\2\61/\3\2\2\2\61"+
		"\60\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\67\3\2\2\2\65"+
		"\63\3\2\2\2\668\5\30\r\2\67\66\3\2\2\2\678\3\2\2\289\3\2\2\29:\7\2\2\3"+
		":\3\3\2\2\2;<\7\4\2\2<=\7\30\2\2=\5\3\2\2\2>?\7\4\2\2?\7\3\2\2\2@A\7\5"+
		"\2\2A\t\3\2\2\2BC\7\6\2\2C\13\3\2\2\2DE\7\7\2\2E\r\3\2\2\2FG\7\b\2\2G"+
		"H\7\27\2\2HI\7\t\2\2I\17\3\2\2\2JK\7\b\2\2KP\7\27\2\2LM\7\n\2\2MO\7\27"+
		"\2\2NL\3\2\2\2OR\3\2\2\2PN\3\2\2\2PQ\3\2\2\2QS\3\2\2\2RP\3\2\2\2ST\7\t"+
		"\2\2T\21\3\2\2\2UW\7\b\2\2VX\7\27\2\2WV\3\2\2\2WX\3\2\2\2XY\3\2\2\2Y["+
		"\7\24\2\2Z\\\7\27\2\2[Z\3\2\2\2[\\\3\2\2\2\\]\3\2\2\2]^\7\t\2\2^\23\3"+
		"\2\2\2_`\7\13\2\2`a\7\30\2\2a\25\3\2\2\2bc\7\b\2\2ch\7\30\2\2de\7\n\2"+
		"\2eg\7\30\2\2fd\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2ik\3\2\2\2jh\3\2"+
		"\2\2kl\7\t\2\2l\27\3\2\2\2mn\7\f\2\2no\5\32\16\2op\7\r\2\2p\31\3\2\2\2"+
		"qr\b\16\1\2rx\5\"\22\2sx\5$\23\2tx\5\36\20\2ux\5 \21\2vx\5\34\17\2wq\3"+
		"\2\2\2ws\3\2\2\2wt\3\2\2\2wu\3\2\2\2wv\3\2\2\2x\u0081\3\2\2\2yz\f\t\2"+
		"\2z{\7\25\2\2{\u0080\5\32\16\n|}\f\b\2\2}~\7\26\2\2~\u0080\5\32\16\t\177"+
		"y\3\2\2\2\177|\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082"+
		"\3\2\2\2\u0082\33\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0085\7\16\2\2\u0085"+
		"\u0086\7\30\2\2\u0086\35\3\2\2\2\u0087\u0088\7\16\2\2\u0088\u0089\7\30"+
		"\2\2\u0089\u008a\7\17\2\2\u008a\u008b\7\27\2\2\u008b\37\3\2\2\2\u008c"+
		"\u008d\7\16\2\2\u008d\u008e\7\30\2\2\u008e\u008f\7\20\2\2\u008f\u0090"+
		"\7\27\2\2\u0090!\3\2\2\2\u0091\u0092\7\16\2\2\u0092\u0093\7\30\2\2\u0093"+
		"\u0094\7\21\2\2\u0094\u0095\7\27\2\2\u0095#\3\2\2\2\u0096\u0097\7\16\2"+
		"\2\u0097\u0098\7\30\2\2\u0098\u0099\7\22\2\2\u0099\u009a\7\30\2\2\u009a"+
		"\u009b\7\23\2\2\u009b%\3\2\2\2\f\61\63\67PW[hw\177\u0081";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}