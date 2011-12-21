package org.kuali.maven.plugins.graph.dot.html;

import java.util.List;

public class TableRow {
    List<TableCell<?>> cells;

    public List<TableCell<?>> getCells() {
        return cells;
    }

    public void setCells(List<TableCell<?>> cells) {
        this.cells = cells;
    }

}
