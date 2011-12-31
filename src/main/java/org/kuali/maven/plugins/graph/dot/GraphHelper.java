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
import org.kuali.maven.plugins.graph.pojo.FilterType;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.LabelContext;
import org.kuali.maven.plugins.graph.pojo.NameValue;
import org.kuali.maven.plugins.graph.util.Helper;

/**
 * Various utility methods for working with Graph pojo's.
 *
 * @author jeffcaddel
 */
public class GraphHelper {
    HtmlUtils htmlUtils = new HtmlUtils();
    public static final String DEFAULT_TYPE = "jar";

    public String getGraphTitle(GraphDescriptor context) {
        String title = context.getShowTitle() ? context.getTitle() : "";

        if (!context.getShowLegend()) {
            return '"' + title + '"';
        }

        List<NameValue> labels = getLegendLabels(context);
        if (Helper.isEmpty(labels)) {
            return '"' + title + '"';
        }
        HtmlUtils htmlUtils = new HtmlUtils();
        Table table = getTitle(title, labels);
        return "<" + htmlUtils.toHtml(table) + ">";
    }

    protected List<NameValue> getLegendLabels(GraphDescriptor context) {
        List<NameValue> labels = new ArrayList<NameValue>();
        addLabel("includes", context.getIncludes(), labels);
        addLabel("excludes", context.getExcludes(), labels);
        addLabel("show", context.getShow(), labels);
        addLabel("hide", context.getHide(), labels);
        if (context.getFilterType() != FilterType.HIDE) {
            addLabel("filter", context.getFilterType().toString(), labels);
        }
        if (!context.getTransitive()) {
            addLabel("transitive", context.getTransitive() + "", labels);
        }
        if (context.getDepth() != null && context.getDepth() >= 0) {
            addLabel("depth", context.getDepth() + "", labels);
        }
        return labels;
    }

    protected void addLabel(String name, String value, List<NameValue> labels) {
        if (!Helper.isBlank(value)) {
            NameValue nv = new NameValue(name, value);
            labels.add(nv);
        }
    }

    protected String getLegendText(NameValue label, int padding) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.leftPad(label.getName(), padding, " "));
        sb.append(" = ");
        sb.append(label.getValue());
        return sb.toString();
    }

    protected TableRow getLegendRow(NameValue label, Font font, int padding) {
        font.setContent(getLegendText(label, padding));
        TableCell cell = new TableCell(htmlUtils.toHtml(font));
        cell.setAlign(CellAlign.LEFT);
        return new TableRow(cell);
    }

    protected int getMaxNameLength(List<NameValue> labels) {
        int max = 0;
        for (NameValue label : labels) {
            int length = label.getName().length();
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    protected List<TableRow> getLegendRows(String title, List<NameValue> labels) {
        Font font = new Font("black", 10);
        // Needs to be a fixed width font
        font.setFace("Courier");
        font.setContent(" ");
        List<TableRow> rows = new ArrayList<TableRow>();
        // Add a blank row for spacing if there is a title to display
        if (!StringUtils.isBlank(title)) {
            rows.add(new TableRow(new TableCell(htmlUtils.toHtml(font))));
        }
        int padding = getMaxNameLength(labels);
        for (NameValue label : labels) {
            rows.add(getLegendRow(label, font, padding));
        }
        // Add a blank row for spacing
        font.setContent(" ");
        rows.add(new TableRow(new TableCell(htmlUtils.toHtml(font))));
        return rows;
    }

    public Table getTitle(String title, List<NameValue> labels) {
        List<TableRow> rows = new ArrayList<TableRow>(getLegendRows(title, labels));
        TableCell titleCell = new TableCell(title);
        TableRow titleRow = new TableRow(titleCell);
        if (!StringUtils.isBlank(title)) {
            rows.add(0, titleRow);
        }
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

    public Graph getGraph(GraphDecorator decorator, List<GraphNode> nodes, List<Edge> edges) {
        Graph graph = new Graph();
        graph.setGraphDecorator(decorator);
        graph.setNodes(nodes);
        graph.setEdges(edges);
        return graph;
    }

    protected void add(List<String> list, String s, boolean show) {
        if (show && !Helper.isBlank(s)) {
            list.add(s);
        }
    }

    public String getLabel(Artifact a) {
        return getLabel(a, new LabelContext());
    }

    public String getLabel(Artifact a, LabelContext context) {

        boolean showType = context.isShowTypes() && !DEFAULT_TYPE.equalsIgnoreCase(a.getType());

        List<String> labelTokens = new ArrayList<String>();
        add(labelTokens, a.getGroupId(), context.isShowGroupIds());
        add(labelTokens, a.getArtifactId(), context.isShowArtifactIds());
        add(labelTokens, a.getType(), showType);
        add(labelTokens, a.getClassifier(), context.isShowClassifiers());
        add(labelTokens, a.getVersion(), context.isShowVersions());
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
