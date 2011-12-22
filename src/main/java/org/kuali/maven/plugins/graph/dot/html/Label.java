package org.kuali.maven.plugins.graph.dot.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

public class Label extends NestedElement {

    Text text;
    Table table;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getElementNames() {
        List<String> names = new ArrayList<String>();
        names.add("text");
        names.add("table");
        return names;
    }

    @Override
    public List<? extends HtmlElement> getElements() {
        Assert.isTrue((text == null || table == null) && (text != null || table != null));
        if (text == null) {
            return Collections.singletonList(table);
        } else {
            return Collections.singletonList(text);
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
