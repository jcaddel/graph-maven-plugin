package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.LabelContext;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Helper;
import org.kuali.maven.plugins.graph.tree.Node;

public class LabelProcessor implements Processor {
    GraphDescriptor graphDescriptor;

    public LabelProcessor() {
        this(null);
    }

    public LabelProcessor(GraphDescriptor graphDescriptor) {
        super();
        this.graphDescriptor = graphDescriptor;
    }

    @Override
    public void process(Node<MavenContext> node) {
        GraphHelper graphHelper = new GraphHelper();
        LabelContext labelContext = Helper.copyProperties(LabelContext.class, graphDescriptor);
        List<Node<MavenContext>> treeNodes = node.getBreadthFirstList();
        for (Node<MavenContext> element : treeNodes) {
            MavenContext mc = element.getObject();
            GraphNode graphNode = mc.getGraphNode();
            Artifact artifact = mc.getArtifact();
            String label = graphHelper.getLabel(artifact, labelContext);
            graphNode.setLabel(label);
        }
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

}
