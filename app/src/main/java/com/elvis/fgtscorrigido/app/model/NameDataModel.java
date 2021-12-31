package com.elvis.fgtscorrigido.app.model;


public class NameDataModel {
    private int id;
    private String name;

    public NameDataModel() {

    }

    public NameDataModel(String pName) {
        name = pName;
    }

    public void setId(int pId) {
        id = pId;
    }

    public void setName(String pName) {
        name = pName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
