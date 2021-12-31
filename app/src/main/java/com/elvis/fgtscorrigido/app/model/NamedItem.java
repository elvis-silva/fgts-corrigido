package com.elvis.fgtscorrigido.app.model;

public class NamedItem {

    String id, toString;
    Object item;

    public NamedItem(String pId, Object pItem, String pToString) {
        if(pId == null || pItem == null) {
            throw new IllegalArgumentException();
        }
        id = pId;
        item = pItem;
        toString = pToString;
    }

    public NamedItem(String pId, Object pItem) {
        this(pId, pItem, null);
    }

    public NamedItem(String pId) {
        id = pId;
    }

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object pItem) {
        item = pItem;
    }

    public String getToString() {
        return toString;
    }

    public  void setToString(String pToString) {
        toString = pToString;
    }

    @Override
    public String toString() {
        return toString != null ? toString : String.valueOf(item);
    }
}
