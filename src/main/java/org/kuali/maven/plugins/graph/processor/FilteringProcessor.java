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
    GraphDescriptor graphContext;

    public FilteringProcessor() {
        this(null);
    }

    public FilteringProcessor(GraphDescriptor graphContext) {
        super();
        this.graphContext = graphContext;
    }

    @Override
    public void process(Node<MavenContext> node) {
        Filter<Node<MavenContext>> filter = getFilter(node);
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

    protected Filter<Node<MavenContext>> getFilter(Node<MavenContext> node) {
        NodeFilter<MavenContext> include = mh.getIncludeFilter(graphContext);
        NodeFilter<MavenContext> exclude = mh.getExcludeFilter(graphContext);
        Filter<Node<MavenContext>> filter = new IncludeExcludeFilter<Node<MavenContext>>(include, exclude);
        return filter;
    }

    public GraphDescriptor getGraphContext() {
        return graphContext;
    }

    public void setGraphContext(GraphDescriptor graphContext) {
        this.graphContext = graphContext;
    }

}
