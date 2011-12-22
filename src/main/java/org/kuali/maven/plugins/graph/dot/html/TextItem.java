package org.kuali.maven.plugins.graph.dot.html;

import org.springframework.util.Assert;

public class TextItem implements HtmlElement {
    String string;
    Br br;
    Font font;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getElementNames() {
        return new String[] { "br", "font" };
    }

    @Override
    public HtmlElement[] getElements() {
        Assert.isTrue((br == null || font == null) && (br != null || font != null));
        if (br == null) {
            return new HtmlElement[] { font };
        } else {
            return new HtmlElement[] { br };
        }
    }

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
