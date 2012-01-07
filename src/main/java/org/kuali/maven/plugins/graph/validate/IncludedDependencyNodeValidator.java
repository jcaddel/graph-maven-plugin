/**
 * Copyright 2010-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.plugins.graph.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.springframework.util.Assert;

/**
 * <p>
 * Perform validation on nodes that Maven has marked as participating in the build.
 * </p>
 *
 * <p>
 * This validates that the set of dependencies in the build are unique with respect to
 * [groupId]:[artifactId]:[type]:[classifier].
 * </p>
 *
 * <p>
 * In layman's terms, this guarantees that only one version of a jar will be used during a build.
 * </p>
 */
public class IncludedDependencyNodeValidator extends DependencyNodeValidator {

    public IncludedDependencyNodeValidator() {
        super(State.INCLUDED);
    }

    @Override
    protected void validateState(List<DependencyNode> nodes) {
        Map<String, Artifact> ids = new HashMap<String, Artifact>();
        Map<String, Artifact> partialIds = new HashMap<String, Artifact>();
        for (DependencyNode node : nodes) {
            Artifact a = node.getArtifact();
            Assert.state(node.getRelatedArtifact() == null, "Included nodes can't contain related artifacts");
            String id = TreeHelper.getArtifactId(a);
            String partialId = TreeHelper.getPartialArtifactId(a);
            ids.put(id, a);
            partialIds.put(partialId, a);
        }
        int c1 = nodes.size();
        int c2 = ids.size();
        int c3 = partialIds.size();

        boolean valid = c1 == c2 && c2 == c3;

        Assert.state(valid, "Unique included artifact id counts don't match.  c1=" + c1 + " c2=" + c2 + " c3=" + c3);
    }

}
