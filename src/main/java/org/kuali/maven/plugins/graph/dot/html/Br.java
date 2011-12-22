package org.kuali.maven.plugins.graph.dot.html;

public class Br {
    public Br() {
        this(null);
    }

    public Br(Align align) {
        super();
        this.align = align;
    }

    Align align;

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

}
