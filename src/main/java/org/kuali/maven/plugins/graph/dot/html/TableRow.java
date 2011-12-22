package org.kuali.maven.plugins.graph.dot.html;

import java.util.List;

public class TableRow {

    public TableRow() {
        this(null);
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
