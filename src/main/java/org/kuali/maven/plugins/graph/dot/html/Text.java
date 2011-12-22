package org.kuali.maven.plugins.graph.dot.html;

public class Text implements HtmlElement {

    TextItem textItem;
    Text text;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public HtmlElement[] elements() {
        return new HtmlElement[] { text, textItem };
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
