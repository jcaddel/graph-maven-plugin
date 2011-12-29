package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class HideDuplicatesProcessor implements Processor {
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> root) {
        List<Node<MavenContext>> duplicates = helper.getNodeList(root, State.DUPLICATE);
        for (Node<MavenContext> duplicate : duplicates) {
            helper.hide(duplicate);
        }
    }
}
