/**
 * Copyright 2010-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.plugins.graph.dot.html;

import java.util.Arrays;
import java.util.List;


public class Table implements HtmlTag {
    HtmlUtils htmlUtils = new HtmlUtils();

    public Table() {
        super();
    }

    public Table(TableRow... rows) {
        this(Arrays.asList(rows));
    }

    public Table(List<TableRow> rows) {
        super();
        this.rows = rows;
    }

    @Override
    public String getName() {
        return "table";
    }

    @Override
    public String getContent() {
        return htmlUtils.toHtml(rows);
    }

    Align align;
    String bgcolor;
    Integer border;
    Integer cellborder;
    Integer cellpadding;
    Integer cellspacing;
    String color;
    Integer columns;
    Boolean fixedsize;
    String height;
    String href;
    String id;
    String port;
    Integer rowCount;
    String style;
    String target;
    String title;
    String tooltip;
    VAlign valign;
    String width;

    List<TableRow> rows;

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Integer getBorder() {
        return border;
    }

    public void setBorder(Integer border) {
        this.border = border;
    }

    public Integer getCellborder() {
        return cellborder;
    }

    public void setCellborder(Integer cellborder) {
        this.cellborder = cellborder;
    }

    public Integer getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(Integer cellpadding) {
        this.cellpadding = cellpadding;
    }

    public Integer getCellspacing() {
        return cellspacing;
    }

    public void setCellspacing(Integer cellspacing) {
        this.cellspacing = cellspacing;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
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

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rows) {
        this.rowCount = rows;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> tableRows) {
        this.rows = tableRows;
    }

}
