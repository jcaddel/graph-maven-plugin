package org.kuali.maven.plugins.graph.validate;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
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
            Artifact a = node.getArtifact();
            Artifact related = node.getRelatedArtifact();

            Assert.notNull(related, "Duplicate nodes must contain related artifacts");

            String id1 = TreeHelper.getArtifactId(a);
            String id2 = TreeHelper.getArtifactId(related);

            if (!id1.equals(id2)) {
                logger.info("fake dup->" + id1);
            }

            String partialId1 = TreeHelper.getPartialArtifactId(a);
            String partialId2 = TreeHelper.getPartialArtifactId(related);

            boolean equal = partialId1.equals(partialId2);

            Assert.state(equal, "Artifact ids must be the same except for version");
        }
        logger.debug("Validated duplicate nodes");
    }

}
