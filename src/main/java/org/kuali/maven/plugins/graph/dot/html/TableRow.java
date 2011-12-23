package org.kuali.maven.plugins.graph.dot.html;

import java.util.Collections;
import java.util.List;

public class TableRow implements HtmlTag {
    HtmlUtils htmlUtils = new HtmlUtils();

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

    @Override
    public String getName() {
        return "tr";
    }

    @Override
    public String getContent() {
        return htmlUtils.toHtml(cells);
    }

    List<TableCell> cells;

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

}
