package org.kuali.maven.plugins.graph.dot.html;

public abstract class SimpleElement implements HtmlElement {

    @Override
    public HtmlElement[] getElements() {
        return new HtmlElement[] {};
    }

    @Override
    public String[] getElementNames() {
        return new String[] {};
    }

}
