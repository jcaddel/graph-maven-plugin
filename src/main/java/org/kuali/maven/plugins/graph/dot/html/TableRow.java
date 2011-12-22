package org.kuali.maven.plugins.graph.dot.html;

import java.util.Collections;
import java.util.List;

public class TableRow implements HtmlElement {

    @Override
    public String getName() {
        return "tr";
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
