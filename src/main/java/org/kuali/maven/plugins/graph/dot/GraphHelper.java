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

import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.util.StringUtils;
import org.kuali.maven.plugins.graph.dot.html.CellAlign;
import org.kuali.maven.plugins.graph.dot.html.Font;
import org.kuali.maven.plugins.graph.dot.html.HtmlUtils;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.kuali.maven.plugins.graph.dot.html.TableCell;
import org.kuali.maven.plugins.graph.dot.html.TableRow;
import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.Hider;
import org.kuali.maven.plugins.graph.pojo.NameValue;
import org.kuali.maven.plugins.graph.tree.Helper;

/**
 * Various utility methods for working with Graph pojo's.
 *
 * @author jeffcaddel
 */
public class GraphHelper {
    HtmlUtils htmlUtils = new HtmlUtils();
    public static final String DEFAULT_TYPE = "jar";

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
        Font font = new Font("black", 10);
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

    protected void add(List<String> list, String s, boolean skip) {
        if (skip || Helper.isBlank(s)) {
            return;
        } else {
            list.add(s);
        }
    }

    public String getLabel(Artifact a) {
        return getLabel(a, new Hider());
    }

    public String getLabel(Artifact a, Hider hider) {

        boolean hideType = hider.isHideType() || DEFAULT_TYPE.equalsIgnoreCase(a.getType());

        List<String> labelTokens = new ArrayList<String>();
        add(labelTokens, a.getGroupId(), hider.isHideGroupId());
        add(labelTokens, a.getArtifactId(), hider.isHideArtifactId());
        add(labelTokens, a.getType(), hideType);
        add(labelTokens, a.getClassifier(), hider.isHideClassifier());
        add(labelTokens, a.getVersion(), hider.isHideVersion());
        return getLabel(labelTokens);
    }

    public String getLabel(List<String> tokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            if (i != 0) {
                sb.append("\\n");
            }
            sb.append(tokens.get(i));
        }
        return sb.toString();
    }
}
