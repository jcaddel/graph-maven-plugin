package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class StyleProcessor implements Processor {
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> node) {
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        for (Node<MavenContext> element : list) {
            helper.updateGraphNodeStyle(element.getObject());
        }
    }
}
