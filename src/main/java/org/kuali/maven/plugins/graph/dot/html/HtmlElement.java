package org.kuali.maven.plugins.graph.dot.html;

public interface HtmlElement {
    String getName();

    HtmlElement[] getElements();

    String[] getElementNames();
}
