package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.kuali.maven.plugins.graph.dot.EdgeGenerator;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.LayoutStyle;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class HideConflictsProcessor implements Processor {
    LayoutStyle layout;
    TreeHelper helper = new TreeHelper();
    EdgeGenerator generator = new EdgeGenerator();

    public HideConflictsProcessor() {
        this(null);
    }

    public HideConflictsProcessor(LayoutStyle layout) {
        super();
        this.layout = layout;
    }

    @Override
    public void process(Node<MavenContext> root) {
        switch (layout) {
        case CONDENSED:
            handleCondensedConflicts(root);
            return;
        case FLAT:
            handleFlatConflicts(root);
            return;
        default:
            throw new IllegalStateException(" Unknown layout " + layout);
        }
    }

    protected void handleCondensedConflicts(Node<MavenContext> root) {
        List<Node<MavenContext>> conflicts = helper.getNodeList(root, State.CONFLICT);
        for (Node<MavenContext> conflict : conflicts) {
            String replacementId = TreeHelper.getArtifactId(conflict.getObject().getReplacement());
            Node<MavenContext> replacement = TreeHelper.findRequiredIncludedNode(root, replacementId);
            Edge edge = generator.getParentChildEdge(conflict, replacement);
            generator.addEdge(conflict.getParent(), edge);
            helper.hide(conflict);
        }
    }

    protected void handleFlatConflicts(Node<MavenContext> root) {
        List<Node<MavenContext>> conflicts = helper.getNodeList(root, State.CONFLICT);
        for (Node<MavenContext> conflict : conflicts) {
            helper.hide(conflict);
        }
    }

    public LayoutStyle getLayout() {
        return layout;
    }

    public void setLayout(LayoutStyle layout) {
        this.layout = layout;
    }
}
