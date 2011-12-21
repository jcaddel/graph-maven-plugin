package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicateDependencyNodeValidator extends OmittedDependencyNodeValidator {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateDependencyNodeValidator.class);

    public DuplicateDependencyNodeValidator() {
        super(State.DUPLICATE);
    }

    @Override
    protected void validateNodes(List<DependencyNode> nodes) {
        super.validateNodes(nodes);
        for (DependencyNode node : nodes) {
            boolean equal = helper.equals(node.getArtifact(), node.getRelatedArtifact());
            if (!equal) {
                logger.debug("fake dup->" + node.getArtifact());
            }
        }
        logger.info("Validated " + state + " nodes");
    }

}
