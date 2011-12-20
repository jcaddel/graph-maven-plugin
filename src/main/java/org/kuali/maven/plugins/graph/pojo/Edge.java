package org.kuali.maven.plugins.graph.pojo;

public class Edge {

    int id;
    boolean hidden;
    GraphNode parent;
    GraphNode child;
    String label = "";
    String style = "solid";
    String color = "black";
    String fontcolor = "black";
    String weight = "1.0";

    public Edge() {
        this(null, null);
    }

    public Edge(GraphNode parent, GraphNode child) {
        super();
        this.parent = parent;
        this.child = child;
    }

    public GraphNode getParent() {
        return parent;
    }

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

    public GraphNode getChild() {
        return child;
    }

    public void setChild(GraphNode child) {
        this.child = child;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}
