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

import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.NameValue;

public class GraphHelper {

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
