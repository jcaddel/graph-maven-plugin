package org.kuali.maven.plugins.graph.dot.html;

import org.springframework.util.Assert;

public class Label implements HtmlElement {

    Text text;
    Table table;

    @Override
    public String getName() {
        return "label";
    }

    @Override
    public String[] getElementNames() {
        return new String[] { "text", "table" };
    }

    @Override
    public HtmlElement[] getElements() {
        Assert.isTrue((text == null || table == null) && (text != null || table != null));
        if (text == null) {
            return new HtmlElement[] { table };
        } else {
            return new HtmlElement[] { text };
        }
    }

    public Label() {
        super();
    }

    public Label(Table table) {
        super();
        this.table = table;
    }

    public Label(Text text) {
        super();
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
