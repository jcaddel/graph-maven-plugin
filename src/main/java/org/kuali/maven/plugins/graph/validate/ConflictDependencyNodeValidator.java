package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Perform validation on nodes Maven marks as <code>State.CONFLICT</state>
 * </p>
 */
public class ConflictDependencyNodeValidator extends OmittedDependencyNodeValidator {
    private static final Logger logger = LoggerFactory.getLogger(ConflictDependencyNodeValidator.class);

    public ConflictDependencyNodeValidator() {
        super(State.CONFLICT);
    }

    @Override
    protected void validateState(List<DependencyNode> nodes) {
        super.validateState(nodes);
        for (DependencyNode node : nodes) {
            boolean equal = helper.equals(node.getArtifact(), node.getRelatedArtifact());
            if (equal) {
                // This really shouldn't happen.
                // It is a confusing way to label nodes in the dependency tree
                // Maven marked this node as a "conflict" but the related artifact is the same version
                // The term "conflict" would seem to imply that this version of this artifact has been
                // conflicted out of the build and replaced with another version
                // Why then, would the related artifact be the same version?
                // This gets cleaned up by the sanitizers, so just log a message here
                logger.debug("fake conflict->" + node.getArtifact());
            }
        }
        logger.debug("Validated " + state + " nodes");
    }

}
