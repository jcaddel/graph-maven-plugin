package org.kuali.maven.plugins.graph.pojo;

import java.util.List;

import com.sun.rowset.internal.Row;

public class Category {
    String label;
    String description;
    List<Row> rows;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
