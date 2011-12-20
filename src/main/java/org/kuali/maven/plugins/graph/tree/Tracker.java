package org.kuali.maven.plugins.graph.tree;

import java.util.HashMap;

public class Tracker extends HashMap<String, Integer> {

    private static final long serialVersionUID = -2649198262211508724L;

    public void increment(String s) {
        if (get(s) == null) {
            put(s, 1);
        } else {
            put(s, get(s) + 1);
        }
    }

}
