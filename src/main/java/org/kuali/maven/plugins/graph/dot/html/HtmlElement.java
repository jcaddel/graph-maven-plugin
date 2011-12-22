package org.kuali.maven.plugins.graph.dot.html;

import java.util.List;

public interface HtmlElement {
    String getName();

    List<? extends HtmlElement> getElements();
}
