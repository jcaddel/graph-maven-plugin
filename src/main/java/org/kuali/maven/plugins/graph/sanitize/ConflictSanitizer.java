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

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Included;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConflictSanitizer extends MavenContextSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(ConflictSanitizer.class);

    public ConflictSanitizer() {
        this(null);
    }

    public ConflictSanitizer(Included included) {
        super(included, State.CONFLICT);
    }

    @Override
    protected void sanitize(MavenContext context, Included included) {
        String id1 = context.getArtifactIdentifier();
        String partialId1 = context.getPartialArtifactIdentifier();
        Node<MavenContext> exact1 = included.getIds().get(id1);
        Node<MavenContext> partial1 = included.getPartialIds().get(partialId1);

        // This is ok. Kind of. Maven has marked it as a conflict, but it should be duplicate
        if (exact1 != null) {
            // Emit an "info" level log message and switch to DUPLICATE
            State switchTo = State.DUPLICATE;
            logger.info(getSwitchMessage(id1, switchTo));
            logger.info("Identical replacement for a 'conflict' artifact");
            context.setState(switchTo);
            context.setReplacement(exact1);
            return;
        }

        if (partial1 != null) {
            // This is ok. We've located a suitable replacement
            logger.debug(id1 + " meets conflict node criteria.");
            context.setReplacement(partial1);
            return;
        }

        Artifact related = context.getDependencyNode().getRelatedArtifact();
        if (related == null) {
            // With no related artifact, there is nothing more we can do
            warnAndSwitch(State.UNKNOWN, id1, context, "No suitable replacement and no related artifact");
            return;
        }

        // Examine the related artifact
        String id2 = TreeHelper.getArtifactId(related);
        String partialId2 = TreeHelper.getPartialArtifactId(related);
        Node<MavenContext> exact2 = included.getIds().get(id2);
        Node<MavenContext> partial2 = included.getPartialIds().get(partialId2);

        if (exact2 != null) {
            // This is ok. We've located a suitable replacement
            logger.debug(id1 + " meets conflict node criteria.");
            context.setReplacement(exact2);
            return;
        }

        if (partial2 != null) {
            // This is ok. We've located a suitable replacement
            logger.debug(id1 + " meets conflict node criteria.");
            context.setReplacement(partial2);
            return;
        }

        // Conflict node has no replacement that we can find
        warnAndSwitch(State.UNKNOWN, id1, context, "No suitable replacement and no related artifact");
    }

}
