package org.leo.json;

public interface JsonPathListener {

    void onValue(Object value, ParsingContext context);

}
