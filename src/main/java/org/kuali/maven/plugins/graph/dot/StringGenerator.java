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

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.EdgeDecorator;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.NodeDecorator;

/**
 * Convert a Graph object into text
 */
public class StringGenerator {

    public String getString(Graph graph) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph dependencies {\n\n");
        sb.append(getString(graph.getGraphDecorator()));
        sb.append(getString(graph.getNodeDecorator()));
        sb.append(getString(graph.getEdgeDecorator()));
        sb.append("\n");
        sb.append(getString(graph.getNodes()));
        sb.append("\n");
        sb.append(toString(graph.getEdges()));
        sb.append("}\n");
        return sb.toString();
    }

    public String getString(GraphDecorator decorator) {
        if (decorator == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  graph [");
        sb.append(" label=" + decorator.getLabel() + "");
        sb.append(" labeljust=\"" + decorator.getLabeljust() + "\"");
        sb.append(" labelloc=\"" + decorator.getLabelloc() + "\"");
        sb.append(" fontsize=\"" + decorator.getFontsize() + "\"");
        sb.append(" fontname=\"" + decorator.getFontname() + "\"");
        sb.append(" ranksep=\"" + decorator.getRanksep() + "\"");
        sb.append(" rankdir=\"" + decorator.getRankdir() + "\"");
        sb.append(" nodesep=\"" + decorator.getNodesep() + "\"");
        sb.append(" ];\n");
        return sb.toString();
    }

    public String getString(NodeDecorator decorator) {
        if (decorator == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  node [");
        sb.append(" fontsize=\"" + decorator.getFontsize() + "\"");
        sb.append(" fontname=\"" + decorator.getFontname() + "\"");
        sb.append(" shape=\"" + decorator.getShape() + "\"");
        sb.append(" ];\n");
        return sb.toString();
    }

    public String getString(EdgeDecorator decorator) {
        if (decorator == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  edge [");
        sb.append(" fontsize=\"" + decorator.getFontsize() + "\"");
        sb.append(" fontname=\"" + decorator.getFontname() + "\"");
        sb.append(" ];\n");
        return sb.toString();
    }

    public String getString(List<GraphNode> nodes) {
        if (nodes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (GraphNode node : nodes) {
            sb.append(getString(node));
        }
        return sb.toString();
    }

    public String getString(GraphNode node) {
        if (node.isHidden()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  \"" + node.getId() + "\" [\n");
        sb.append("    fontsize=\"" + node.getFontsize() + "\"\n");
        sb.append("    label=\"" + node.getLabel() + "\"\n");
        sb.append("    color=\"" + node.getColor() + "\"\n");
        sb.append("    fontcolor=\"" + node.getFontcolor() + "\"\n");
        sb.append("    fillcolor=\"" + node.getFillcolor() + "\"\n");
        sb.append("    style=\"" + node.getStyle() + "\"\n");
        sb.append("  ];\n");
        return sb.toString();
    }

    public String toString(List<Edge> edges) {
        if (edges == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Edge edge : edges) {
            sb.append(getString(edge));
        }
        return sb.toString();
    }

    public String getString(Edge edge) {
        GraphNode parent = edge.getParent();
        GraphNode child = edge.getChild();
        if (parent.isHidden() || child.isHidden()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  \"" + parent.getId() + "\" -> \"" + child.getId() + "\" [\n");
        sb.append("    label=\"" + edge.getLabel() + "\"\n");
        sb.append("    style=\"" + edge.getStyle() + "\"\n");
        sb.append("    color=\"" + edge.getColor() + "\"\n");
        sb.append("    fontcolor=\"" + edge.getFontcolor() + "\"\n");
        sb.append("    weight=\"" + edge.getWeight() + "\"\n");
        sb.append("  ];\n");
        return sb.toString();
    }
}
