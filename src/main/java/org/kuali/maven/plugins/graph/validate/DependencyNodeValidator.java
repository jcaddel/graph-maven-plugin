package org.kuali.maven.plugins.graph.validate;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.springframework.util.Assert;

/**
 * <p>
 * Abstraction for validation logic that is common to all dependency nodes in the tree regardless of what
 * <code>State</code> they are marked with.
 * </p>
 *
 * <p>
 * Extending classes implement the validateState() method to provide validation for dependency nodes marked with a
 * specific <code>State</code>
 * </p>
 *
 * @author jeffcaddel
 */
public abstract class DependencyNodeValidator implements NodeValidator<MavenContext> {
    TreeHelper helper = new TreeHelper();
    State state;

    public DependencyNodeValidator(State state) {
        super();
        this.state = state;
    }

    protected abstract void validateState(List<DependencyNode> nodes);

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
        validateState(nodes);
    }

    public State getState() {
        return state;
    }

}
