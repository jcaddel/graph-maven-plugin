package org.kuali.maven.plugins.graph.processor;

import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.mojo.MojoHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class HidingProcessor implements Processor {
    MojoHelper mh = new MojoHelper();
    TreeHelper helper = new TreeHelper();
    GraphDescriptor graphDescriptor;

    public HidingProcessor() {
        this(null);
    }

    public HidingProcessor(GraphDescriptor graphDescriptor) {
        super();
        this.graphDescriptor = graphDescriptor;
    }

    @Override
    public void process(Node<MavenContext> node) {
        Filter<Node<MavenContext>> filter = mh.getIncludeExcludeFilter(graphDescriptor);
        filter(node, filter);
    }

    public void filter(Node<MavenContext> node, Filter<Node<MavenContext>> filter) {
        boolean hide = !filter.isMatch(node) && !node.isRoot();
        if (hide) {
            helper.hideTree(node);
        } else {
            for (Node<MavenContext> child : node.getChildren()) {
                filter(child, filter);
            }
        }
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

}
