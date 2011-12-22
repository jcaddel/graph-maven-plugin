package org.kuali.maven.plugins.graph.dot.html;

public class TextItem {
    String string;
    Br br;
    Font font;

    public TextItem() {
        super();
    }

    public TextItem(String string) {
        super();
        this.string = string;
    }

    public TextItem(Br br) {
        super();
        this.br = br;
    }

    public TextItem(Font font) {
        super();
        this.font = font;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Br getBr() {
        return br;
    }

    public void setBr(Br br) {
        this.br = br;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
