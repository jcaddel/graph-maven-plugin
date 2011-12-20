package org.kuali.maven.plugins.graph.pojo;

public class Style {
    public static final Style DEFAULT_STYLE = new Style();

    String color;
    String fontcolor;
    String fillcolor;
    String weight;
    String fontsize;
    String style;

    public Style() {
        this(null, null, null, null, null, null);
    }

    public Style(String color, String fontcolor, String fillcolor, String weight, String fontsize, String style) {
        super();
        this.color = color;
        this.fontcolor = fontcolor;
        this.fillcolor = fillcolor;
        this.weight = weight;
        this.fontsize = fontsize;
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getFillcolor() {
        return fillcolor;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
