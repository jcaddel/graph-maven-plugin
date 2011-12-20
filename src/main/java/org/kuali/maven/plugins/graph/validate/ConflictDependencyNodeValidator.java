package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ConflictDependencyNodeValidator extends DependencyNodeValidator {
    private static final Logger logger = LoggerFactory.getLogger(ConflictDependencyNodeValidator.class);

    public ConflictDependencyNodeValidator() {
        super(State.CONFLICT);
    }

    @Override
    protected void validateDependencyNodes(List<DependencyNode> nodes) {
        for (DependencyNode node : nodes) {
            Artifact artifact = node.getArtifact();
            Artifact related = node.getRelatedArtifact();

            Assert.notNull(related, state + " nodes must contain related artifacts");

            boolean equal = helper.equals(artifact, related);
            boolean similar = helper.similar(artifact, related);

            if (equal) {
                logger.debug("fake conflict->" + artifact);
            }

            Assert.state(similar, "Artifact's must be the same except for version");
        }
        logger.debug("Validated " + state + " nodes");
    }

}
