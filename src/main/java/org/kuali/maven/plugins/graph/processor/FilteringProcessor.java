package org.kuali.maven.plugins.graph.processor;

import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.filter.IncludeExcludeFilter;
import org.kuali.maven.plugins.graph.filter.NodeFilter;
import org.kuali.maven.plugins.graph.mojo.MojoHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class FilteringProcessor implements Processor {
    MojoHelper mh = new MojoHelper();
    TreeHelper helper = new TreeHelper();
    GraphDescriptor graphDescriptor;

    public FilteringProcessor() {
        this(null);
    }

    public FilteringProcessor(GraphDescriptor graphDescriptor) {
        super();
        this.graphDescriptor = graphDescriptor;
    }

    @Override
    public void process(Node<MavenContext> node) {
        Filter<Node<MavenContext>> filter = getIncludeExcludeFilter(node);
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

    protected Filter<Node<MavenContext>> getIncludeExcludeFilter(Node<MavenContext> node) {
        NodeFilter<MavenContext> include = mh.getIncludeFilter(graphDescriptor);
        NodeFilter<MavenContext> exclude = mh.getExcludeFilter(graphDescriptor);
        return new IncludeExcludeFilter<Node<MavenContext>>(include, exclude);
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

}
