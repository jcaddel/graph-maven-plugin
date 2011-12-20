package org.kuali.maven.plugins.graph.pojo;

public class NodeDecorator {
    String fontsize = "8";
    String fontname = "SanSerif";
    String shape = "rectangle";

    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getFontname() {
        return fontname;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
}
