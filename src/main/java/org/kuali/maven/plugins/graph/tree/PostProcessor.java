package org.kuali.maven.plugins.graph.tree;

import java.util.List;

import org.kuali.maven.plugins.graph.GraphContext;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;

public interface PostProcessor {

    public void process(GraphContext context, Node<MavenContext> node, List<Edge> edges, List<GraphNode> nodes);

}
