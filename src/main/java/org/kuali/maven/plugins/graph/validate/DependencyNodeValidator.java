package org.kuali.maven.plugins.graph.validate;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.springframework.util.Assert;

public abstract class DependencyNodeValidator implements NodeValidator<MavenContext> {
    TreeHelper helper = new TreeHelper();
    State state;

    public DependencyNodeValidator(State state) {
        super();
        this.state = state;
    }

    protected abstract void validateDependencyNodes(List<DependencyNode> nodes);

    @Override
    public void validate(Node<MavenContext> node) {
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        List<DependencyNode> nodes = new ArrayList<DependencyNode>();
        for (Node<MavenContext> element : list) {
            DependencyNode dn = element.getObject().getDependencyNode();
            Assert.state(dn.getArtifact() != null, "Artifact for a dependency node can't be null");
            State elementState = State.getState(dn.getState());
            if (state == elementState) {
                nodes.add(dn);
            }
        }
        validateDependencyNodes(nodes);
    }

    public State getState() {
        return state;
    }

}
