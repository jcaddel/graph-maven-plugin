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
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.kuali.maven.plugins.graph.dot.html.Font;
import org.kuali.maven.plugins.graph.dot.html.HtmlUtils;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.kuali.maven.plugins.graph.dot.html.TableCell;
import org.kuali.maven.plugins.graph.dot.html.TableRow;
import org.kuali.maven.plugins.graph.dot.html.enums.CellAlign;
import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.NameValue;

public class GraphHelper {
    HtmlUtils htmlUtils = new HtmlUtils();

    protected String getLegendText(NameValue label) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.leftPad(label.getName(), 10, " "));
        sb.append(" = ");
        sb.append(label.getValue());
        return sb.toString();
    }

    protected TableRow getLegendRow(NameValue label, Font font) {
        font.setContent(getLegendText(label));
        TableCell cell = new TableCell(htmlUtils.toHtml(font));
        cell.setAlign(CellAlign.LEFT);
        return new TableRow(cell);
    }

    protected List<TableRow> getLegendRows(List<NameValue> labels) {
        Font font = new Font("black", 8);
        font.setFace("Courier New");
        font.setContent(" ");
        List<TableRow> rows = new ArrayList<TableRow>();
        // Add a blank row for spacing
        rows.add(new TableRow(new TableCell(htmlUtils.toHtml(font))));
        for (NameValue label : labels) {
            rows.add(getLegendRow(label, font));
        }
        // Add a blank row for spacing
        font.setContent(" ");
        rows.add(new TableRow(new TableCell(htmlUtils.toHtml(font))));
        return rows;
    }

    public Table getTitle(String title, List<NameValue> labels) {
        TableCell titleCell = new TableCell(title);
        TableRow titleRow = new TableRow(titleCell);
        List<TableRow> rows = new ArrayList<TableRow>(getLegendRows(labels));
        rows.add(0, titleRow);
        Table table = new Table(rows);
        table.setBorder(0);
        table.setCellpadding(0);
        table.setCellspacing(0);
        return table;
    }

    protected List<TableRow> getTableRows(List<NameValue> labels) {
        List<TableRow> rows = new ArrayList<TableRow>();
        for (NameValue label : labels) {
            TableCell cell1 = new TableCell(label.getName());
            cell1.setWidth("15");
            cell1.setAlign(CellAlign.RIGHT);
            TableCell cell2 = new TableCell("=");
            TableCell cell3 = new TableCell(label.getValue());
            cell3.setAlign(CellAlign.LEFT);
            rows.add(new TableRow(cell1, cell2, cell3));
        }
        return rows;
    }

    public Graph getGraph(String title) {
        return getGraph(title, Direction.DEFAULT_DIRECTION, new ArrayList<GraphNode>(), new ArrayList<Edge>());
    }

    public Graph getGraph(String title, List<GraphNode> nodes) {
        return getGraph(title, Direction.DEFAULT_DIRECTION, nodes, new ArrayList<Edge>());
    }

    public Graph getGraph(String title, Direction direction, List<GraphNode> nodes, List<Edge> edges) {
        GraphDecorator decorator = new GraphDecorator(title, direction.name());
        return getGraph(decorator, nodes, edges);
    }

    public Graph getGraph(Table title, Direction direction, List<GraphNode> nodes, List<Edge> edges) {
        GraphDecorator decorator = new GraphDecorator(title, direction.name());
        return getGraph(decorator, nodes, edges);
    }

    public Graph getGraph(GraphDecorator decorator, List<GraphNode> nodes, List<Edge> edges) {
        Graph graph = new Graph();
        graph.setGraphDecorator(decorator);
        graph.setNodes(nodes);
        graph.setEdges(edges);
        return graph;
    }

}
