package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class DuplicateDependencyNodeValidator extends DependencyNodeValidator {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateDependencyNodeValidator.class);

    public DuplicateDependencyNodeValidator() {
        super(State.DUPLICATE);
    }

    @Override
    protected void validateDependencyNodes(List<DependencyNode> nodes) {
        for (DependencyNode node : nodes) {
            Artifact artifact = node.getArtifact();
            Artifact related = node.getRelatedArtifact();

            Assert.notNull(related, state + " nodes must contain related artifacts");

            boolean equals = helper.equals(artifact, related);
            boolean similar = helper.similar(artifact, related);

            if (!equals) {
                logger.debug("fake dup->" + artifact);
            }
            Assert.state(similar, "Artifact's must be the same except for version");
        }
        logger.debug("Validated " + state + " nodes");
    }

}
