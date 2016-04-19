/**
 * Copyright 2011-2013 The Kuali Foundation
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
import org.kuali.maven.plugins.graph.pojo.Display;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.NameValueColor;
import org.kuali.maven.plugins.graph.processor.ColorRuleExtractor;
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

        List<NameValueColor> labels = getLegendLabels(context);
        if (Helper.isEmpty(labels)) {
            return '"' + title + '"';
        }
        Table table = getTitle(title, labels);
        return "<" + htmlUtils.toHtml(table) + ">";
    }

    protected List<NameValueColor> getLegendLabels(GraphDescriptor context) {
        List<NameValueColor> labels = new ArrayList<NameValueColor>();
        addLabel("includes", context.getIncludes(), null, labels);
        addLabel("excludes", context.getExcludes(), null, labels);
        addLabel("show", context.getShow(), null, labels);
        addLabel("hide", context.getHide(), null, labels);
        if (context.getDisplay() != Display.TREE) {
            addLabel("display", context.getDisplay().toString(), null, labels);
        }
        if (!context.getTransitive()) {
            addLabel("transitive", context.getTransitive() + "", null, labels);
        }
        if (context.getDepth() != null && context.getDepth() >= 0) {
            addLabel("depth", context.getDepth() + "", null, labels);
        }
        if (!context.getColorRules().isEmpty()) {
            for (ColorRuleExtractor.ColorRule rule : new ColorRuleExtractor().getColorRules(context.getColorRules())) {
                addLabel(rule.getTarget().name(), rule.getPattern(), rule.getColor(), labels);
            }
        }
        return labels;
    }

    protected void addLabel(String name, String value, String color, List<NameValueColor> labels) {
        if (!Helper.isBlank(value)) {
            labels.add(new NameValueColor(name, value, color));
        }
    }

    protected TableRow getLegendRow(NameValueColor legendEntry) {
        TableCell right = createTableCell(CellAlign.LEFT, legendEntry.getValue());
        right.setBgcolor(legendEntry.getColor());

        return new TableRow(
            createTableCell(CellAlign.RIGHT, legendEntry.getName()),
            createTableCell(null, "="),
            right
        );
    }

    protected TableRow getSpacingRow() {
        return new TableRow(
            createTableCell(CellAlign.RIGHT, null),
            createTableCell(null, null),
            createTableCell(CellAlign.LEFT, null)
        );
    }

    private TableCell createTableCell(CellAlign align, String content) {
        Font font = new Font("black", 10);
        // Needs to be a fixed width font
        font.setFace("Courier");
        font.setContent(" ");

        if (content == null) {
            return new TableCell("");
        }
        font.setContent(content);
        TableCell result = new TableCell(htmlUtils.toHtml(font));
        if (align != null) {
            result.setAlign(align);
        }
        return result;
    }

    public Table getTitle(String title, List<NameValueColor> labels) {
        Table table = new Table(getRows(title, labels));
        table.setBorder(0);
        table.setCellpadding(0);
        table.setCellspacing(0);
        return table;
    }

    private List<TableRow> getRows(String title, List<NameValueColor> labels) {
        List<TableRow> result = new ArrayList<TableRow>();
        if (!StringUtils.isBlank(title)) {
            TableCell tableCell = new TableCell(title);
            tableCell.setColspan(3);
            result.add(new TableRow(tableCell));
        }

        // Add a blank row for spacing if there is a title to display
        if (!StringUtils.isBlank(title)) {
            result.add(getSpacingRow());
        }
        for (NameValueColor label : labels) {
            result.add(getLegendRow(label));
        }
        // Add a blank row for spacing
        result.add(getSpacingRow());
        return result;
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

    protected boolean isTrue(Boolean flag) {
        return Boolean.TRUE.equals(flag);
    }

    public String getLabel(Artifact a) {
        GraphDescriptor d = new GraphDescriptor();
        d.setShowTypes(true);
        d.setShowGroupIds(true);
        d.setShowArtifactIds(true);
        d.setShowVersions(true);
        d.setShowClassifiers(true);
        return getLabel(a, d);
    }

    public String getLabel(Artifact a, GraphDescriptor d) {

        boolean showType = isTrue(d.getShowTypes()) && !DEFAULT_TYPE.equalsIgnoreCase(a.getType());

        List<String> labelTokens = new ArrayList<String>();
        add(labelTokens, a.getGroupId(), isTrue(d.getShowGroupIds()));
        add(labelTokens, a.getArtifactId(), isTrue(d.getShowArtifactIds()));
        add(labelTokens, a.getType(), showType);
        add(labelTokens, a.getClassifier(), isTrue(d.getShowClassifiers()));
        add(labelTokens, a.getVersion(), isTrue(d.getShowVersions()));
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
