package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.springframework.util.Assert;

/**
 * <p>
 * Abstraction for performing validation on nodes that have been excluded from participating in the build.
 * </p>
 */
public abstract class OmittedDependencyNodeValidator extends DependencyNodeValidator {

    public OmittedDependencyNodeValidator(State state) {
        super(state);
    }

    @Override
    protected void validateNodes(List<DependencyNode> nodes) {
        for (DependencyNode node : nodes) {
            Assert.notNull(node.getRelatedArtifact(), state + " nodes must contain related artifacts");

            boolean similar = helper.similar(node.getArtifact(), node.getRelatedArtifact());

            Assert.state(similar, "Artifact's must be the same except for version");
        }
    }

}
