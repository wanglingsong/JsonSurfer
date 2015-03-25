package org.leo.json.path;

import java.util.Iterator;
import java.util.LinkedList;

public class JsonPath {

    // TODO recycling node for saving gc

    private LinkedList<PathOperator> path = new LinkedList<PathOperator>();

    public boolean match(JsonPath jsonPath) {
        // TODO exact matching for the first version
        // TODO maybe reversed comparison is faster
        if (path.size() != jsonPath.path.size()) {
            return false;
        }
        // no need to compare root node
        Iterator<PathOperator> iterator1 = path.iterator();
        Iterator<PathOperator> iterator2 = jsonPath.path.iterator();
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            PathOperator o1 = iterator1.next();
            PathOperator o2 = iterator2.next();
            if (!o1.match(o2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }

    public static JsonPath buildPath() {
        JsonPath newPath = new JsonPath();
        newPath.path.push(Root.instance());
        return newPath;
    }

    public JsonPath child(String key) {
        path.push(new ChildNode(key));
        return this;
    }

    public JsonPath childWildcard() {
        path.push(ChildWildcard.instance());
        return this;
    }

    public JsonPath array() {
        path.push(new ArrayIndex());
        return this;
    }

    public JsonPath arrayWildcard() {
        path.push(ArrayWildcard.instance());
        return this;
    }

    public JsonPath array(int index) {
        path.push(new ArrayIndex(index));
        return this;
    }

    public PathOperator pop() {
        return path.pop();
    }

    public PathOperator peek() {
        return path.peek();
    }

    public void clear() {
        path.clear();
    }

    public int pathDepth() {
        return this.path.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<PathOperator> dItr = path.descendingIterator();
        while (dItr.hasNext()) {
            sb.append(dItr.next());
        }
        return sb.toString();
    }
}
