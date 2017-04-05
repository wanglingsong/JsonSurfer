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

package org.jsfr.json.compiler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jsfr.json.filter.*;
import org.jsfr.json.path.JsonPath;

import java.math.BigDecimal;

/**
 * Created by Leo on 2015/4/1.
 */
public class JsonPathCompiler extends JsonPathBaseVisitor<Void> {

    private JsonPath.Builder builder;

    private FilterBuilder filterBuilder;

    public JsonPath.Builder getBuilder() {
        return builder;
    }

    @Override
    public Void visitPath(JsonPathParser.PathContext ctx) {
        builder = JsonPath.Builder.start();
        return super.visitPath(ctx);
    }

    @Override
    public Void visitSearchChild(JsonPathParser.SearchChildContext ctx) {
        builder.scan().child(ctx.KEY().getText());
        return super.visitSearchChild(ctx);
    }

    @Override
    public Void visitSearch(JsonPathParser.SearchContext ctx) {
        builder.scan();
        return super.visitSearch(ctx);
    }

    @Override
    public Void visitChildNode(JsonPathParser.ChildNodeContext ctx) {
        builder.child(ctx.KEY().getText());
        return super.visitChildNode(ctx);
    }

    @Override
    public Void visitChildrenNode(JsonPathParser.ChildrenNodeContext ctx) {
        int i = 0;
        String[] keys = new String[ctx.KEY().size()];
        for (TerminalNode key : ctx.KEY()) {
            keys[i++] = key.getText();
        }
        builder.children(keys);
        return super.visitChildren(ctx);
    }

    @Override
    public Void visitIndex(JsonPathParser.IndexContext ctx) {
        builder.index(Integer.parseInt(ctx.NUM().getText()));
        return super.visitIndex(ctx);
    }

    @Override
    public Void visitIndexes(JsonPathParser.IndexesContext ctx) {
        int i = 0;
        Integer[] keys = new Integer[ctx.NUM().size()];
        for (TerminalNode key : ctx.NUM()) {
            keys[i++] = Integer.parseInt(key.getText());
        }
        builder.indexes(keys);
        return super.visitIndexes(ctx);
    }

    @Override
    public Void visitSlicing(JsonPathParser.SlicingContext ctx) {
        Integer left = null;
        Integer right;
        Integer temp = null;
        for (ParseTree node:ctx.children) {
            if (node instanceof TerminalNode) {
                TerminalNode tNode = (TerminalNode) node;
                if (((TerminalNode) node).getSymbol().getType() == JsonPathParser.COLON) {
                    left = temp;
                    temp = null;
                } else if (tNode.getSymbol().getType() == JsonPathParser.NUM) {
                    temp = Integer.parseInt(tNode.getText());
                }
            }
        }
        right = temp;
        builder.slicing(left, right);
        return super.visitSlicing(ctx);
    }

    @Override
    public Void visitAnyChild(JsonPathParser.AnyChildContext ctx) {
        builder.anyChild();
        return super.visitAnyChild(ctx);
    }

    @Override
    public Void visitAnyIndex(JsonPathParser.AnyIndexContext ctx) {
        builder.anyIndex();
        return super.visitAnyIndex(ctx);
    }

    @Override
    public Void visitAny(JsonPathParser.AnyContext ctx) {
        builder.any();
        return super.visitAny(ctx);
    }

    @Override
    public Void visitFilter(JsonPathParser.FilterContext ctx) {
        builder.any();
        filterBuilder = new FilterBuilder();
        Void rst = super.visitFilter(ctx);
        builder.withFilter(filterBuilder.build());
        return rst;
    }

    @Override
    public Void visitExpr(JsonPathParser.ExprContext ctx) {
        Void rst;
        if (ctx.AndOperator() != null) {
            filterBuilder.startAndPredicate();
            rst = super.visitExpr(ctx);
            filterBuilder.endAndPredicate();
        } else if (ctx.OrOperator() != null) {
            filterBuilder.startOrPredicate();
            rst = super.visitExpr(ctx);
            filterBuilder.endOrPredicate();
        } else {
            rst = super.visitExpr(ctx);
        }
        return rst;
    }

    @Override
    public Void visitExprEqualNum(JsonPathParser.ExprEqualNumContext ctx) {
        JsonPath relativePath = JsonPath.Builder.start().child(ctx.KEY().getText()).build();
        filterBuilder.append(new EqualityNumPredicate( relativePath, new BigDecimal(ctx.NUM().getText())));
        return super.visitExprEqualNum(ctx);
    }

    @Override
    public Void visitExprExist(JsonPathParser.ExprExistContext ctx) {
        JsonPath relativePath = JsonPath.Builder.start().child(ctx.KEY().getText()).build();
        filterBuilder.append(new ExistencePredicate(relativePath));
        return super.visitExprExist(ctx);
    }

    @Override
    public Void visitExprGtNum(JsonPathParser.ExprGtNumContext ctx) {
        JsonPath relativePath = JsonPath.Builder.start().child(ctx.KEY().getText()).build();
        filterBuilder.append(new GreaterThanNumPredicate(relativePath, new BigDecimal(ctx.NUM().getText())));
        return super.visitExprGtNum(ctx);
    }

    @Override
    public Void visitExprLtNum(JsonPathParser.ExprLtNumContext ctx) {
        JsonPath relativePath = JsonPath.Builder.start().child(ctx.KEY().getText()).build();
        filterBuilder.append(new LessThanNumPredicate( relativePath, new BigDecimal(ctx.NUM().getText())));
        return super.visitExprLtNum(ctx);
    }

    @Override
    public Void visitExprEqualStr(JsonPathParser.ExprEqualStrContext ctx) {
        JsonPath relativePath = JsonPath.Builder.start().child(ctx.KEY(0).getText()).build();
        filterBuilder.append(new EqualityStrPredicate( relativePath, ctx.KEY(1).getText()));
        return super.visitExprEqualStr(ctx);
    }

    public static JsonPath[] compile(String... paths) {
        JsonPath[] jsonPaths = new JsonPath[paths.length];
        for (int i = 0; i < paths.length; i++) {
            jsonPaths[i] = compile(paths[i]);
        }
        return jsonPaths;
    }

    public static JsonPath compile(String path) {
        JsonPathLexer lexer = new JsonPathLexer(new ANTLRInputStream(path));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JsonPathParser parser = new JsonPathParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        JsonPathParser.PathContext tree = parser.path();
        JsonPathCompiler compiler = new JsonPathCompiler();
        compiler.visit(tree);
        return compiler.getBuilder().build();
    }

//    public static void main(String[] s) {
//        JsonPath path = compile("$..abc.c.d[1].e[2,3,6]");
//        System.out.println(path);
//    }

}
