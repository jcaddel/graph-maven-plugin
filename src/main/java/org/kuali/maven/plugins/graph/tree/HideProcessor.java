package org.kuali.maven.plugins.graph.tree;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.GraphContext;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.Hider;
import org.kuali.maven.plugins.graph.pojo.MavenContext;

public class HideProcessor implements TreeProcessor {

    @Override
    public void process(GraphContext context, Node<MavenContext> node, List<GraphNode> nodes, List<Edge> edges) {
        GraphHelper graphHelper = new GraphHelper();
        Hider hider = getHider(context);
        List<Node<MavenContext>> treeNodes = node.getBreadthFirstList();
        for (Node<MavenContext> element : treeNodes) {
            MavenContext mc = element.getObject();
            GraphNode graphNode = mc.getGraphNode();
            Artifact artifact = mc.getArtifact();
            String label = graphHelper.getLabel(artifact, hider);
            graphNode.setLabel(label);
        }
    }

    protected Hider getHider(GraphContext gc) {
        Hider hider = new Hider();
        hider.setHideGroupId(!gc.isShowGroupIds());
        return hider;
    }

}
