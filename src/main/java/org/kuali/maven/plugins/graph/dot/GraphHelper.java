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

import org.kuali.maven.plugins.graph.dot.html.Label;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.kuali.maven.plugins.graph.dot.html.TableCell;
import org.kuali.maven.plugins.graph.dot.html.TableRow;
import org.kuali.maven.plugins.graph.dot.html.Text;
import org.kuali.maven.plugins.graph.dot.html.TextItem;
import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.NameValue;

public class GraphHelper {

    public Table getLegendTable(String title, List<NameValue> labels) {
        Text text = new Text(new TextItem(title));
        Label label = new Label(text);
        TableCell cell = new TableCell(label);
        TableRow row = new TableRow(Collections.singletonList(cell));
        Table table = new Table(Collections.singletonList(row));
        table.setBorder("1");
        return table;
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
