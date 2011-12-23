package org.kuali.maven.plugins.graph.dot.html;

import org.kuali.maven.plugins.graph.dot.html.enums.Align;

public class Br implements HtmlTag {

    public Br() {
        this(null);
    }

    public Br(Align align) {
        super();
        this.align = align;
    }

    @Override
    public String getName() {
        return "br";
    }

    @Override
    public String getContent() {
        return null;
    }

    Align align;

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

}
