package org.kuali.maven.plugins.graph.dot.html;

public class Text {

    TextItem textItem;
    Text text;

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
