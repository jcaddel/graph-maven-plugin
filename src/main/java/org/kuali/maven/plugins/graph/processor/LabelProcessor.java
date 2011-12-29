package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.pojo.GraphContext;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.LabelContext;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Helper;
import org.kuali.maven.plugins.graph.tree.Node;

public class LabelProcessor implements Processor {
    GraphContext graphContext;

    public LabelProcessor() {
        this(null);
    }

    public LabelProcessor(GraphContext graphContext) {
        super();
        this.graphContext = graphContext;
    }

    @Override
    public void process(Node<MavenContext> node) {
        GraphHelper graphHelper = new GraphHelper();
        LabelContext labelContext = Helper.copyProperties(LabelContext.class, graphContext);
        List<Node<MavenContext>> treeNodes = node.getBreadthFirstList();
        for (Node<MavenContext> element : treeNodes) {
            MavenContext mc = element.getObject();
            GraphNode graphNode = mc.getGraphNode();
            Artifact artifact = mc.getArtifact();
            String label = graphHelper.getLabel(artifact, labelContext);
            // String label = graphNode.getId() + "\\n" + artifact.getArtifactId() + "\\n" + artifact.getVersion();
            graphNode.setLabel(label);
        }
    }

    public GraphContext getGraphContext() {
        return graphContext;
    }

    public void setGraphContext(GraphContext graphContext) {
        this.graphContext = graphContext;
    }

}
