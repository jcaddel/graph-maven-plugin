package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.mojo.MojoHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class ShowPathsProcessor implements Processor {
    MojoHelper mh = new MojoHelper();
    TreeHelper helper = new TreeHelper();
    GraphDescriptor graphDescriptor;

    public ShowPathsProcessor() {
        this(null);
    }

    public ShowPathsProcessor(GraphDescriptor graphDescriptor) {
        super();
        this.graphDescriptor = graphDescriptor;
    }

    @Override
    public void process(Node<MavenContext> node) {
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        List<Node<MavenContext>> displayed = new ArrayList<Node<MavenContext>>();
        Filter<Node<MavenContext>> filter = mh.getIncludeFilter(graphDescriptor);
        for (Node<MavenContext> element : list) {
            helper.hide(element);
            boolean display = filter.isMatch(element) || element.isRoot();
            if (display) {
                displayed.add(element);
            }
        }
        for (Node<MavenContext> element : displayed) {
            helper.showPath(element);
        }
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

}
