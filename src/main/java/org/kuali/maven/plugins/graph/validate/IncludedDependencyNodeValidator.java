package org.kuali.maven.plugins.graph.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.springframework.util.Assert;

public class IncludedDependencyNodeValidator extends DependencyNodeValidator {

    public IncludedDependencyNodeValidator() {
        super(State.INCLUDED);
    }

    @Override
    protected void validateDependencyNodes(List<DependencyNode> nodes) {
        Map<String, Artifact> ids = new HashMap<String, Artifact>();
        Map<String, Artifact> partialIds = new HashMap<String, Artifact>();
        for (DependencyNode node : nodes) {
            Artifact a = node.getArtifact();
            boolean isNull = node.getRelatedArtifact() == null;
            Assert.state(isNull, "Included nodes can't contain related artifacts");
            String id = TreeHelper.getArtifactId(a);
            String partialId = TreeHelper.getPartialArtifactId(a);
            ids.put(id, a);
            partialIds.put(partialId, a);
        }
        int c1 = nodes.size();
        int c2 = ids.size();
        int c3 = partialIds.size();

        boolean equal = c1 == c2 && c2 == c3;

        Assert.state(equal, "Unique included artifact id counts don't match.  c1=" + c1 + " c2=" + c2 + " c3=" + c3);
    }

}
