package org.kuali.maven.plugins.graph.dot.html;

import java.util.Collections;
import java.util.List;

public abstract class SimpleElement implements HtmlElement {

    @Override
    public List<HtmlElement> getElements() {
        return Collections.emptyList();
    }

}
