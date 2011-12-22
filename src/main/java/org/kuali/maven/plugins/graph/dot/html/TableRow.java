package org.kuali.maven.plugins.graph.dot.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableRow extends NestedElement {

    @Override
    public String getName() {
        return "tr";
    }

    @Override
    public List<String> getElementNames() {
        List<String> names = new ArrayList<String>();
        names.add("cells");
        return names;
    }

    @Override
    public List<? extends HtmlElement> getElements() {
        return cells;
    }

    public TableRow() {
        super();
    }

    public TableRow(TableCell cell) {
        this(Collections.singletonList(cell));
    }

    public TableRow(List<TableCell> cells) {
        super();
        this.cells = cells;
    }

    List<TableCell> cells;

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

}
