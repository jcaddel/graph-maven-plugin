package org.kuali.maven.plugins.graph.dot.html;

public class Label implements HtmlElement {

    Text text;
    Table table;

    @Override
    public String getName() {
        return "label";
    }

    @Override
    public HtmlElement[] elements() {
        return new HtmlElement[] { text, table };
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
