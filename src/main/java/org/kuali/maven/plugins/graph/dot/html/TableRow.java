package org.kuali.maven.plugins.graph.dot.html;

import java.util.List;

public class TableRow {
    public TableRow() {
        this(null);
    }

    public TableRow(List<? extends TableCell<?>> cells) {
        super();
        this.cells = cells;
    }

    List<? extends TableCell<?>> cells;

    public List<? extends TableCell<?>> getCells() {
        return cells;
    }

    public void setCells(List<? extends TableCell<?>> cells) {
        this.cells = cells;
    }

}
