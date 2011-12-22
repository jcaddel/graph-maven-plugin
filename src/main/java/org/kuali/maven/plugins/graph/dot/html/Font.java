package org.kuali.maven.plugins.graph.dot.html;

public class Font implements HtmlElement {
    String color;
    String face;
    String pointSize;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getPointSize() {
        return pointSize;
    }

    public void setPointSize(String pointSize) {
        this.pointSize = pointSize;
    }

}
