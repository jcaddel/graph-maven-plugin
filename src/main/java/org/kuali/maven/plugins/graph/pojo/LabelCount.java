package org.kuali.maven.plugins.graph.pojo;

public class LabelCount implements Comparable<LabelCount> {
    String label;
    int count;

    public LabelCount() {
        this(null, 0);
    }

    public LabelCount(String label, int count) {
        super();
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(LabelCount other) {
        return count > other.count ? 1 : count < other.count ? -1 : 0;
    }

}
