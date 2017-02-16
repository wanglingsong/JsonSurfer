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

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JsonPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JsonPathVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(JsonPathParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#searchChild}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchChild(JsonPathParser.SearchChildContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#search}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch(JsonPathParser.SearchContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#anyChild}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyChild(JsonPathParser.AnyChildContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#anyIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyIndex(JsonPathParser.AnyIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#any}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny(JsonPathParser.AnyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(JsonPathParser.IndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#indexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexes(JsonPathParser.IndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#slicing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlicing(JsonPathParser.SlicingContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#childNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChildNode(JsonPathParser.ChildNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#childrenNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChildrenNode(JsonPathParser.ChildrenNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(JsonPathParser.FilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(JsonPathParser.ExprContext ctx);
}