package org.kuali.maven.plugins.graph.dot.html;

public class Font {
    String color;
    String face;
    String pointSize;
    Text text;

    public Font() {
        this(null);
    }

    public Font(Text text) {
        super();
        this.text = text;
    }

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

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
