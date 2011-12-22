package org.kuali.maven.plugins.graph.dot.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SimpleElement implements HtmlElement {

    @Override
    public List<HtmlElement> getElements() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getElementNames() {
        return Collections.emptyList();
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        List<String> excludes = new ArrayList<String>(getElementNames());
        excludes.add("name");
        excludes.add("elements");
        excludes.add("elementNames");
        String attributes = HtmlUtils.toHtml(HtmlUtils.getAttributes(this, excludes));
        sb.append("<" + getName() + attributes + "/>");
        return sb.toString();
    }
}
