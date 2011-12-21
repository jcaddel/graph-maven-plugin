/**
 * Copyright 2010-2011 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.sanitize;

import static org.kuali.maven.plugins.graph.pojo.State.CONFLICT;
import static org.kuali.maven.plugins.graph.pojo.State.DUPLICATE;
import static org.kuali.maven.plugins.graph.pojo.State.INCLUDED;

import java.util.List;
import java.util.Map;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * This sanitizer takes into account the overall build tree. After RelatedArtifactSanitizer completes its basic node
 * level checks, we are assured that:<br>
 *
 * 1) Nodes marked as 'conflicts' have a main artifact that is similar to its related artifact, yet is a different
 * version.<br>
 * 2) Nodes Maven originally marked as 'duplicates' have the exact same artifact version stored for both the main and
 * related artifacts.
 *
 * Since we now have this type of consistency in the omitted nodes we need to check to make sure the omitted artifacts
 * actually do have a replacement in the build tree.
 */

public class BuildSanitizer implements NodeSanitizer<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(BuildSanitizer.class);
    TreeHelper helper = new TreeHelper();

    @Override
    public void sanitize(Node<MavenContext> node) {
        // Extract what Maven has included in the build tree
        List<MavenContext> included = helper.getList(node, INCLUDED);

        // Extract the semi-sanitized duplicates
        List<MavenContext> duplicates = helper.getList(node, DUPLICATE);

        // Extract the semi-sanitized conflicts
        List<MavenContext> conflicts = helper.getList(node, CONFLICT);

        // Finish restoring sanity
        for (MavenContext duplicate : duplicates) {
            sanitizeDuplicate(duplicate, included);
        }
        for (MavenContext conflict : conflicts) {
            sanitizeConflict(conflict, included);
        }
    }

    protected void sanitizeDuplicate(MavenContext duplicate, List<MavenContext> included) {
        // Keyed by [groupId]:[artifactId]:[type]:[classifier]:[version]
        Map<String, MavenContext> ids = helper.getMap(included);

        // Keyed by [groupId]:[artifactId]:[type]:[classifier]
        Map<String, MavenContext> partialIds = helper.getPartialIdMap(included);

        // We should be able to find an exact match in the build tree for true duplicates
        MavenContext exact = ids.get(duplicate.getArtifactIdentifier());
        if (exact != null) {
            // We found an exact match
            logger.debug("verified duplicate {}", duplicate.getArtifact());
            return;
        }

        // No exact match, fall back to a similar match
        String partialId = TreeHelper.getPartialArtifactId(duplicate.getArtifact());

        // We should be able to find a similar match
        MavenContext similar = partialIds.get(partialId);
        if (similar != null) {
            // We did find a match, but this now qualifies as a conflict.
            // The artifact is going to be switched out for another version of the same artifact

            // Set state to conflict and store the artifact that replaced us
            duplicate.setState(State.CONFLICT);
            duplicate.setReplacement(similar.getArtifact());
            logger.info("duplicate->conflict {}", duplicate.getArtifact());
        } else {
            // This is just weird.
            // Maven has somehow marked this node as a duplicate, yet there is no version of this
            // artifact participating in the build.
            logger.warn("duplicate->unknown {}", duplicate.getArtifact());
            duplicate.setState(State.UNKNOWN);
        }
    }

    protected void sanitizeConflict(MavenContext conflict, List<MavenContext> included) {
        // Keyed by [groupId]:[artifactId]:[type]:[classifier]:[version]
        Map<String, MavenContext> ids = helper.getMap(included);

        // Keyed by [groupId]:[artifactId]:[type]:[classifier]
        Map<String, MavenContext> partialIds = helper.getPartialIdMap(included);

        if (conflict.getReplacement() == null) {
            logger.error(conflict.getArtifact() + "");
        }

        String partialId1 = TreeHelper.getPartialArtifactId(conflict.getReplacement());
        String partialId2 = TreeHelper.getPartialArtifactId(conflict.getArtifact());

        // Validation logic assures us this is true, but no harm in double checking
        Assert.isTrue(partialId1.equals(partialId2), "Invalid state");

        // Attempt to locate exact replacements for both main and related
        MavenContext exactMain = ids.get(conflict.getArtifactIdentifier());
        String replacementId = TreeHelper.getArtifactId(conflict.getReplacement());
        MavenContext exactRelated = ids.get(replacementId);

        // Either main or related would yield the same similar artifact
        MavenContext similar = partialIds.get(partialId1);

        // If all 3 are null, there is trouble
        if (exactMain == null && exactRelated == null && similar == null) {
            conflict.setState(State.UNKNOWN);
            logger.warn("bs:conflict->unknown {}", conflict.getArtifactIdentifier());
            return;
        } else if (exactMain != null) {
            // Maven marked this as a conflict, yet the exact same version is in fact participating in the build
            // Switch to duplicate since the artifact for this node is in the build
            conflict.setState(State.DUPLICATE);
            conflict.setReplacement(null);
            logger.warn("bs:conflict->duplicate {}", conflict.getArtifactIdentifier());
        } else if (exactRelated != null) {
            // This is the normal condition we would expect for conflicts
            // Maven marked it as a conflict, told us what artifact it conflicted with
            // and we found that artifact in the build tree
            conflict.setState(State.CONFLICT);
            conflict.setReplacement(similar.getArtifact());
            logger.debug("bs:verified conflict {}", conflict.getArtifactIdentifier());
        } else if (similar != null) {
            // This is not normal, but it's ok. Kind of. Maven marked it as a conflict and gave us the artifact
            // it conflicted with. That exact artifact is not participating in the build, but a similar one is.
            // We will accept this, and use the similar artifact instead of the one Maven handed us.
            conflict.setState(State.CONFLICT);
            conflict.setReplacement(similar.getArtifact());
            String id2 = TreeHelper.getArtifactId(similar.getArtifact());
            logger.warn("bs:conflict->conflict {}->{}", replacementId, id2);
        } else {
            Assert.isTrue(false, "Invalid state");
        }
    }

}
