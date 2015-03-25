package org.leo.json.parse;

/**
 * Created by Administrator on 2015/3/25.
 */
public interface JsonProvider<O, A, V> {

    public O createObject();

    public A createArray();

    public boolean isObject(Object object);

    public boolean isArray(Object array);

    public void consumeObjectEntry(O object, String key, V value);

    public void consumeArrayElement(A array, V value);

    public V primitive(Object value);

}
