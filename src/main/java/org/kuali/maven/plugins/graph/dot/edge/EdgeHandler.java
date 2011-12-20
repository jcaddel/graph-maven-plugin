package org.kuali.maven.plugins.graph.dot.edge;

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;

public interface EdgeHandler {

    List<Edge> getEdges(Node<MavenContext> node);

}
