package org.kuali.maven.plugins.graph.dot.html;

import java.util.ArrayList;
import java.util.List;

public abstract class NestedElement implements HtmlElement {

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        if (getName() != null) {
            List<String> excludes = new ArrayList<String>(getElementNames());
            excludes.add("name");
            excludes.add("elements");
            excludes.add("elementNames");
            String attributes = HtmlUtils.toHtml(HtmlUtils.getAttributes(this, excludes));
            sb.append("<" + getName() + attributes + ">");
        }
        for (HtmlElement element : getElements()) {
            sb.append(element.toHtml());
        }
        if (getName() != null) {
            sb.append("</" + getName() + ">");
        }
        return sb.toString();
    }
}
