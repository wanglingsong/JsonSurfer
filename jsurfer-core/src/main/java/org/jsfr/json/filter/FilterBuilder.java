/*
 * MIT License
 *
 * Copyright (c) 2019 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jsfr.json.filter;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Leo on 2017/4/4.
 */
public class FilterBuilder {

    private Deque<AggregatePredicate> stack = new ArrayDeque<AggregatePredicate>();

    private JsonPathFilter resultFilter;

    public FilterBuilder startNegationPredicate() {
        NegationPredicate negationPredicate = new NegationPredicate();
        if (!this.stack.isEmpty()) {
            this.append(negationPredicate);
        }
        this.stack.push(negationPredicate);
        return this;
    }

    public FilterBuilder endNegationAndPredicate() {
        this.resultFilter = this.stack.pop();
        return this;
    }

    public FilterBuilder startAndPredicate() {
        AndPredicate newPredicate = new AndPredicate();
        if (!this.stack.isEmpty()) {
            this.append(newPredicate);
        }
        this.stack.push(newPredicate);
        return this;
    }

    public FilterBuilder endAndPredicate() {
        this.resultFilter = this.stack.pop();
        return this;
    }

    public FilterBuilder startOrPredicate() {
        OrPredicate newPredicate = new OrPredicate();
        if (!this.stack.isEmpty()) {
            this.append(newPredicate);
        }
        this.stack.push(newPredicate);
        return this;
    }

    public FilterBuilder endOrPredicate() {
        this.resultFilter = this.stack.pop();
        return this;
    }

    public FilterBuilder append(JsonPathFilter filter) {
        if (this.stack.isEmpty()) {
            this.resultFilter = filter;
        } else {
            this.stack.peek().addFilter(filter);
        }
        return this;
    }

    public JsonPathFilter build() {
        return this.resultFilter;
    }

}
