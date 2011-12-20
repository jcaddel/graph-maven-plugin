package org.kuali.maven.plugins.graph.filter;

import org.kuali.maven.plugins.graph.tree.Node;

public class ReverseNodeFilter<T> extends ReverseFilter<Node<T>> implements NodeFilter<T> {

    public ReverseNodeFilter() {
        super();
    }

    public ReverseNodeFilter(Filter<Node<T>> filter) {
        super(filter);
    }

}
