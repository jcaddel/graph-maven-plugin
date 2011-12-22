package org.kuali.maven.plugins.graph.dot.html;

import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

public class Font implements HtmlElement {
    String color;
    String face;
    String pointSize;
    Text text;

    @Override
    public String getName() {
        return "font";
    }

    @Override
    public List<? extends HtmlElement> getElements() {
        Assert.isTrue(text != null);
        return Collections.singletonList(text);
    }

    @Override
    public List<String> getElementNames() {
        return Collections.singletonList("text");
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<" + getName() + HtmlUtils.getAttributes(this, getElementNames()) + ">");
        for (HtmlElement element : getElements()) {
            sb.append(element.toHtml());
        }
        sb.append("</" + getName() + ">");
        return sb.toString();
    }

    public Font() {
        this(null);
    }

    public Font(Text text) {
        this(text, null, null);
    }

    public Font(Text text, String color, String pointSize) {
        this(text, color, null, pointSize);
    }

    public Font(Text text, String color, String face, String pointSize) {
        super();
        this.text = text;
        this.color = color;
        this.face = face;
        this.pointSize = pointSize;
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
