package org.kuali.maven.plugins.graph.dot.html;

import java.util.Collections;
import java.util.List;

public class TextItem implements HtmlElement {
    String string;
    Br br;
    Font font;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<? extends HtmlElement> getElements() {
        if (br != null) {
            return Collections.singletonList(br);
        } else if (font != null) {
            return Collections.singletonList(font);
        } else {
            return Collections.emptyList();
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
