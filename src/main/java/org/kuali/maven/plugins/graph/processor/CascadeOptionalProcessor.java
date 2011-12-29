package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class CascadeOptionalProcessor implements Processor {
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> root) {
        List<Node<MavenContext>> list = root.getBreadthFirstList();
        for (Node<MavenContext> element : list) {
            boolean optional = element.getObject().isOptional();
            if (optional) {
                cascadeOptional(element);
            }
        }
    }

    protected void cascadeOptional(Node<MavenContext> node) {
        node.getObject().setOptional(true);
        for (Node<MavenContext> child : node.getChildren()) {
            cascadeOptional(child);
        }
    }
}
