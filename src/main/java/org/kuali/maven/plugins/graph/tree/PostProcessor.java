package org.kuali.maven.plugins.graph.tree;

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;

public interface PostProcessor {

    public void postProcess(Node<MavenContext> node, List<GraphNode> nodes, List<Edge> edges);

}
