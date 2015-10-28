// Generated from JsonPath.g4 by ANTLR 4.5.1

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