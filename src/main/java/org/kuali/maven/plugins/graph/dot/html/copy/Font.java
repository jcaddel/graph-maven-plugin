package org.kuali.maven.plugins.graph.dot.html.copy;

public class Font implements HtmlTag {
    String color;
    String face;
    Integer pointSize;
    String content;

    public Font() {
        this(null, null);
    }

    public Font(String color, Integer pointSize) {
        this(color, null, pointSize);
    }

    public Font(String color, String face, Integer pointSize) {
        this(null, color, face, pointSize);
    }

    public Font(String content, String color, String face, Integer pointSize) {
        super();
        this.content = content;
        this.color = color;
        this.face = face;
        this.pointSize = pointSize;
    }

    @Override
    public String getName() {
        return "font";
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

    public Integer getPointSize() {
        return pointSize;
    }

    public void setPointSize(Integer pointSize) {
        this.pointSize = pointSize;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
