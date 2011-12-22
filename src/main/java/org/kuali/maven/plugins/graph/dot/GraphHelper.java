/**
 * Copyright 2010-2011 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.dot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.kuali.maven.plugins.graph.dot.html.LabelTableCell;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.kuali.maven.plugins.graph.dot.html.TableCell;
import org.kuali.maven.plugins.graph.dot.html.TableRow;
import org.kuali.maven.plugins.graph.dot.html.Text;
import org.kuali.maven.plugins.graph.dot.html.TextItem;
import org.kuali.maven.plugins.graph.dot.html.TextItemLabel;
import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.NameValue;
import org.kuali.maven.plugins.graph.tree.Helper;

public class GraphHelper {

    public Table getLegendTable(String title, List<NameValue> labels) {
        Text text = new Text(title);
        TextItem<Text> textItem = new TextItem<Text>(text);
        TextItemLabel label = new TextItemLabel(textItem);
        LabelTableCell cell = new LabelTableCell(label);
        TableRow row = new TableRow(Collections.singletonList(cell));
        Table table = new Table(Collections.singletonList(row));
        table.setBorder("1");
        return table;
    }

    public String toHtmlTableCellElement(Object object) {
        return "";
    }

    public String toHtml(TableCell<?> cell) {
        StringBuilder sb = new StringBuilder();
        sb.append("<TD" + getAttributes(cell, "class", "element") + ">");
        sb.append(toHtmlTableCellElement(cell.getElement()));
        sb.append("</TD>");
        return sb.toString();
    }

    public String toHtmlTableCells(List<? extends TableCell<?>> cells) {
        if (Helper.isEmpty(cells)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (TableCell<?> cell : cells) {
            sb.append(toHtml(cell));
        }
        return sb.toString();
    }

    public String toHtml(List<TableRow> rows) {
        if (Helper.isEmpty(rows)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (TableRow row : rows) {
            sb.append(toHtml(row));
        }
        return sb.toString();
    }

    public String toHtml(TableRow row) {
        if (Helper.isEmpty(row.getCells())) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<TR>");
        sb.append(toHtmlTableCells(row.getCells()));
        sb.append("</TR>");
        return sb.toString();
    }

    public String toHtml(Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("<TABLE" + getAttributes(table, "class", "rows", "font") + ">");
        sb.append(toHtml(table.getRows()));
        sb.append("</TABLE>");
        return sb.toString();
    }

    protected <T> String getAttributes(T object, String... excludes) {
        Map<String, Object> description = describe(object, excludes);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String key : description.keySet()) {
            Object value = description.get(key);
            if (value != null) {
                if (count++ != 0) {
                    sb.append(" ");
                }
                sb.append(key.toUpperCase() + "=" + quote(value.toString()));
            }
        }
        if (count > 0) {
            return " " + sb.toString();
        } else {
            return sb.toString();
        }
    }

    protected String quote(String s) {
        return '"' + s + '"';
    }

    protected <T> Map<String, Object> describe(T bean, String... excludes) {
        Map<String, Object> description = describe(bean);
        for (String exclude : excludes) {
            description.remove(exclude);
        }
        return description;
    }

    @SuppressWarnings("unchecked")
    protected <T> Map<String, Object> describe(T bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    public String getLegendTitle(String title, List<NameValue> labels) {
        StringBuilder sb = new StringBuilder();
        sb.append("< ");
        sb.append("<TABLE BORDER=\"0\">");
        sb.append("<TR>");
        sb.append("<TD ALIGN=\"LEFT\">" + title + "</TD>");
        sb.append("</TR>");
        for (NameValue label : labels) {
            sb.append(getLabel(label));
        }
        sb.append("</TABLE>");
        sb.append(" >");
        return sb.toString();
    }

    protected String getLabel(NameValue label) {
        StringBuilder sb = new StringBuilder();
        sb.append("<TR>");
        sb.append("<TD ALIGN=\"LEFT\">");
        sb.append("<FONT COLOR=\"#007CCC\" POINT-SIZE=\"12\">");
        sb.append(label.getName() + "=" + label.getValue());
        sb.append("</FONT>");
        sb.append("</TD>");
        sb.append("</TR>");
        return sb.toString();
    }

    public Graph getGraph(String title) {
        return getGraph(title, Direction.DEFAULT_DIRECTION, new ArrayList<GraphNode>(), new ArrayList<Edge>());
    }

    public Graph getGraph(String title, List<GraphNode> nodes) {
        return getGraph(title, Direction.DEFAULT_DIRECTION, nodes, new ArrayList<Edge>());
    }

    public Graph getGraph(String title, Direction direction, List<GraphNode> nodes, List<Edge> edges) {
        GraphDecorator decorator = new GraphDecorator();
        decorator.setRankdir(direction.name());
        decorator.setLabel(title);
        Graph graph = new Graph();
        graph.setGraphDecorator(decorator);
        graph.setNodes(nodes);
        graph.setEdges(edges);
        return graph;
    }

}
