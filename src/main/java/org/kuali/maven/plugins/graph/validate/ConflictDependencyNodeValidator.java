package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Perform validation on nodes Maven has marked as <code>State.CONFLICT</state>
 * </p>
 */
public class ConflictDependencyNodeValidator extends OmittedDependencyNodeValidator {
    private static final Logger logger = LoggerFactory.getLogger(ConflictDependencyNodeValidator.class);

    public ConflictDependencyNodeValidator() {
        super(State.CONFLICT);
    }

    @Override
    protected void validateNodes(List<DependencyNode> nodes) {
        super.validateNodes(nodes);
        for (DependencyNode node : nodes) {
            boolean equal = helper.equals(node.getArtifact(), node.getRelatedArtifact());
            if (equal) {
                logger.debug("fake conflict->" + node.getArtifact());
            }
        }
        logger.debug("Validated " + state + " nodes");
    }

}
