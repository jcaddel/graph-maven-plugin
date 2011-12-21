package org.kuali.maven.plugins.graph.dot.html;

public class TableCell<T> {
    public TableCell() {
        this(null);
    }

    public TableCell(T element) {
        super();
        this.element = element;
    }

    TableCellAlign align;
    TableCellBrAlign balign;
    String bgcolor;
    String border;
    String cellpadding;
    String cellspacing;
    String color;
    int colspan;
    boolean fixedsize;
    String height;
    String href;
    String id;
    String port;
    int rowspan;
    String target;
    String title;
    String tooltip;
    VerticalAlignment valign;
    String width;
    T element;

    public TableCellAlign getAlign() {
        return align;
    }

    public void setAlign(TableCellAlign align) {
        this.align = align;
    }

    public TableCellBrAlign getBalign() {
        return balign;
    }

    public void setBalign(TableCellBrAlign balign) {
        this.balign = balign;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }

    public String getCellspacing() {
        return cellspacing;
    }

    public void setCellspacing(String cellspacing) {
        this.cellspacing = cellspacing;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public boolean isFixedsize() {
        return fixedsize;
    }

    public void setFixedsize(boolean fixedsize) {
        this.fixedsize = fixedsize;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public VerticalAlignment getValign() {
        return valign;
    }

    public void setValign(VerticalAlignment valign) {
        this.valign = valign;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

}
