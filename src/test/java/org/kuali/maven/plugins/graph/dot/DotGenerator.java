package org.kuali.maven.plugins.graph.dot;

import org.kuali.maven.plugins.graph.pojo.Graph;


public class DotGenerator {

    public Graph getHelloWorld() {
        Graph graph = new Graph();
        graph.getGraphDecorator().setLabel("Hello World");
        return graph;
    }

}
