package org.kuali.maven.plugins.graph.filter;

import java.util.List;

import org.kuali.maven.plugins.graph.tree.Node;

/**
 *
 */

public class NodeFilterChain<T> extends FilterChain<Node<T>> implements NodeFilter<T> {

    public NodeFilterChain(List<? extends Filter<Node<T>>> filters, MatchCondition condition,
            boolean defaultReturnValue) {
        super(filters, condition, defaultReturnValue);
    }

    public NodeFilterChain(List<? extends Filter<Node<T>>> filters, MatchCondition condition) {
        super(filters, condition);
    }

    public NodeFilterChain() {
        super();
    }

    public NodeFilterChain(List<NodeFilter<T>> filters) {
        super(filters);
    }

}
