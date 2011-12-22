package org.kuali.maven.plugins.graph.dot.html;

import org.springframework.util.Assert;

public class Text implements HtmlElement {

    TextItem textItem;
    Text text;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public HtmlElement[] elements() {
        Assert.isTrue((text == null || textItem == null) && (text != null || textItem != null));
        if (text == null) {
            return new HtmlElement[] { textItem };
        } else {
            return new HtmlElement[] { text };
        }
    }

    public Text() {
        super();
    }

    public Text(TextItem textItem) {
        super();
        this.textItem = textItem;
    }

    public Text(Text text) {
        super();
        this.text = text;
    }

    public TextItem getTextItem() {
        return textItem;
    }

    public void setTextItem(TextItem textItem) {
        this.textItem = textItem;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
