package org.kuali.maven.plugins.graph.dot.html;

import org.kuali.maven.plugins.graph.dot.html.enums.Align;
import org.kuali.maven.plugins.graph.dot.html.enums.CellAlign;
import org.kuali.maven.plugins.graph.dot.html.enums.VAlign;

public class TableCell implements HtmlTag {

    public TableCell() {
        this(null);
    }

    public TableCell(String content) {
        super();
        this.content = content;
    }

    @Override
    public String getName() {
        return "td";
    }

    // Attributes
    CellAlign align;
    Align balign;
    String bgcolor;
    String border;
    String cellpadding;
    String cellspacing;
    String color;
    Integer colspan;
    Boolean fixedsize;
    String height;
    String href;
    String id;
    String port;
    Integer rowspan;
    String target;
    String title;
    String tooltip;
    VAlign valign;
    String width;

    // content
    String content;

    public CellAlign getAlign() {
        return align;
    }

    public void setAlign(CellAlign align) {
        this.align = align;
    }

    public Align getBalign() {
        return balign;
    }

    public void setBalign(Align balign) {
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

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public Boolean getFixedsize() {
        return fixedsize;
    }

    public void setFixedsize(Boolean fixedsize) {
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

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
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

    public VAlign getValign() {
        return valign;
    }

    public void setValign(VAlign valign) {
        this.valign = valign;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
