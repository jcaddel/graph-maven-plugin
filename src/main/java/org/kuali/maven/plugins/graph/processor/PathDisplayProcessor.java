package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.mojo.MojoHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

/**
 * This processor searches the entire tree for nodes that match the filter. Any nodes that match are collected into a
 * list. Once the list is created, the path from each node back to the root is displayed along with the entire tree
 * below the node.
 *
 * @author jeffcaddel
 *
 */
public class PathDisplayProcessor implements Processor {
    public static final boolean DEFAULT_SHOW_SUB_TREES_VALUE = true;
    MojoHelper mh = new MojoHelper();
    TreeHelper helper = new TreeHelper();
    GraphDescriptor graphDescriptor;
    boolean showSubTrees = DEFAULT_SHOW_SUB_TREES_VALUE;

    public PathDisplayProcessor() {
        this(null);
    }

    public PathDisplayProcessor(GraphDescriptor graphDescriptor) {
        this(graphDescriptor, DEFAULT_SHOW_SUB_TREES_VALUE);
    }

    public PathDisplayProcessor(GraphDescriptor graphDescriptor, boolean showSubTrees) {
        super();
        this.graphDescriptor = graphDescriptor;
        this.showSubTrees = showSubTrees;
    }

    @Override
    public void process(Node<MavenContext> node) {
        Filter<Node<MavenContext>> filter = mh.getIncludeExcludeFilter(graphDescriptor);
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        List<Node<MavenContext>> displayed = new ArrayList<Node<MavenContext>>();
        for (Node<MavenContext> element : list) {
            helper.hide(element);
            boolean display = filter.isMatch(element) || element.isRoot();
            if (display) {
                displayed.add(element);
            }
        }
        for (Node<MavenContext> element : displayed) {
            helper.showPath(element);
            if (!element.isRoot() && showSubTrees) {
                helper.showTree(element);
            }
        }
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

    public boolean isShowSubTrees() {
        return showSubTrees;
    }

    public void setShowSubTrees(boolean showSubTrees) {
        this.showSubTrees = showSubTrees;
    }

}
