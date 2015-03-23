package org.leo.json;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/3/21.
 */
public class ParsingObserver implements ContentHandler {

    LinkedList<ContentHandler> observers = new LinkedList<ContentHandler>();

    @Override
    public void startJSON() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            observer.startJSON();
        }
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            observer.endJSON();
        }
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.startObject()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.endObject()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.startObjectEntry(key)) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.endObjectEntry()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.startArray()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.endArray()) {
                itr.remove();
            }
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (observers.isEmpty()) {
            return true;
        }
        Iterator<ContentHandler> itr = observers.iterator();
        while (itr.hasNext()) {
            ContentHandler observer = itr.next();
            if (!observer.primitive(value)) {
                itr.remove();
            }
        }
        return true;
    }

    public void addObserver(ContentHandler contentHandler) {
        observers.add(contentHandler);
    }
}
