package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Perform validation on nodes Maven has marked as <code>State.DUPLICATE</state>
 * </p>
 */
public class DuplicateDependencyNodeValidator extends OmittedDependencyNodeValidator {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateDependencyNodeValidator.class);

    public DuplicateDependencyNodeValidator() {
        super(State.DUPLICATE);
    }

    @Override
    protected void validateState(List<DependencyNode> nodes) {
        super.validateState(nodes);
        for (DependencyNode node : nodes) {
            boolean equal = helper.equals(node.getArtifact(), node.getRelatedArtifact());
            if (!equal) {
                // This really shouldn't happen.
                // It is a confusing way to label nodes in the dependency tree
                // Maven marked this node as a "duplicate" but the related artifact is a different version
                // The term "duplicate" would reasonably seem to imply that this version of this artifact is
                // participating in the build.
                // Why then, would the related artifact be a different version?
                // This gets cleaned up by the sanitizers, so just log a message here
                logger.debug("fake dup->" + node.getArtifact());
            }
        }
        logger.debug("Validated " + state + " nodes");
    }

}
