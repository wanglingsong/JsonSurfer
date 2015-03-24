package org.leo.json.parse;

public interface JsonPathListener {

    void onValue(Object value, ParsingContext context);

}
