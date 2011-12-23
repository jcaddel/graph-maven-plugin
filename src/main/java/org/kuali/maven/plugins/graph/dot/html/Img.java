package org.kuali.maven.plugins.graph.dot.html;

import org.kuali.maven.plugins.graph.dot.html.enums.Scale;

public class Img implements HtmlTag {
    public Img() {
        this(null, null);
    }

    public Img(Scale scale, String src) {
        super();
        this.scale = scale;
        this.src = src;
    }

    Scale scale;
    String src;

    @Override
    public String getName() {
        return "img";
    }

    @Override
    public String getContent() {
        return null;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
