package org.kuali.maven.plugins.graph.filter;

import org.kuali.maven.plugins.graph.tree.Node;

/**
 * <p>
 * Return true for nodes that are at a level in the tree less than or equal to <code>maxDepth</code>
 * </p>
 *
 * <p>
 * <code>DepthFilter.infinite()</code> matches all nodes.<br>
 * <code>DepthFilter.none()</code> matches no nodes.<br>
 * <code>DepthFilter.root()</code> matches only the root node.<br>
 * </p>
 *
 * <p>
 * Infinite depth is used if no <code>maxDepth</code> is provided.
 * </p>
 *
 */
public class DepthFilter<T> implements NodeFilter<T> {
    public static final int INFINITE = Integer.MAX_VALUE;
    public static final int NONE = -1;
    public static final int ROOT = 0;
    int maxDepth;

    public DepthFilter() {
        this(INFINITE);
    }

    public DepthFilter(int maxDepth) {
        super();
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean isMatch(Node<T> node) {
        return node.getLevel() <= maxDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public static final <T> DepthFilter<T> infinite() {
        return new DepthFilter<T>(INFINITE);
    }

    public static final <T> DepthFilter<T> none() {
        return new DepthFilter<T>(NONE);
    }

    public static final <T> DepthFilter<T> root() {
        return new DepthFilter<T>(ROOT);
    }

}
