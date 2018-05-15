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
