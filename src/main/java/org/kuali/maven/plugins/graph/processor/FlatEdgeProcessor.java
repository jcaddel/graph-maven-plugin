package org.kuali.maven.plugins.graph.processor;

import org.kuali.maven.plugins.graph.dot.EdgeGenerator;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;

public class FlatEdgeProcessor implements Processor {
    EdgeGenerator generator = new EdgeGenerator();

    @Override
    public void process(Node<MavenContext> node) {
        for (Node<MavenContext> child : node.getChildren()) {
            // Create an edge running from the child's parent to itself
            Edge edge = generator.getParentChildEdge(child);
            // Add the edge to the parent's list
            generator.addEdge(child.getParent(), edge);
            // Continue processing the tree
            process(child);
        }
    }

}
