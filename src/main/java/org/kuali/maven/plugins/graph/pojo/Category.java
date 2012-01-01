package org.kuali.maven.plugins.graph.pojo;

import java.util.List;

public class Category {
    String name;
    String description;
    List<Row> rows;

    public Category() {
        this(null);
    }

    public Category(String name) {
        this(name, null);
    }

    public Category(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
