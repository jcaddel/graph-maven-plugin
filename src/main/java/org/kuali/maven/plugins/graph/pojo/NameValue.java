package org.kuali.maven.plugins.graph.pojo;

public class NameValue {
    String name;
    String value;

    public NameValue() {
        this(null, null);
    }

    public NameValue(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
