package com.elvis.fgtscorrigido.app.utils;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String name;
    public String label;
    public final List<String> children = new ArrayList<>();

    public Group(String name, String label) {
        this.name = name;
        this.label = label;
    }
}