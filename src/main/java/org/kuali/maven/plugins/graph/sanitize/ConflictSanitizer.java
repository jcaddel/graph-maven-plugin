/**
 * Copyright 2004-2011 The Kuali Foundation
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

import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConflictSanitizer extends MavenContextSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(ConflictSanitizer.class);

    public ConflictSanitizer() {
        this(null);
    }

    public ConflictSanitizer(Map<String, MavenContext> included) {
        super(included, State.CONFLICT);
    }

    @Override
    protected void sanitize(MavenContext context, Map<String, MavenContext> included) {
        String artifactId = TreeHelper.getArtifactId(context.getDependencyNode().getArtifact());
        MavenContext replacement = included.get(artifactId);

        // This is ok. Kind of. Maven has marked it as a conflict, but it should be duplicate
        if (replacement != null) {
            // Emit an "info" level log message and switch to DUPLICATE
            State switchTo = State.DUPLICATE;
            logger.info(getSwitchMessage(artifactId, switchTo));
            logger.info("Identical replacement for a 'conflict' artifact");
            context.setState(switchTo);
            return;
        }

        // Conflict with no related artifact is not ok
        Artifact related = context.getDependencyNode().getRelatedArtifact();
        if (related == null) {
            warnAndSwitch(State.UNKNOWN, artifactId, context, "No related artifact");
            return;
        }

        // Examine the related artifact
        String relatedArtifactId = TreeHelper.getArtifactId(related);
        replacement = included.get(relatedArtifactId);
        if (replacement != null) {
            // This is ok. We located the replacement
            logger.debug(artifactId + " meets conflict node criteria.");
            return;
        } else {
            // This is not ok. The related artifact is not in the included list
            warnAndSwitch(State.UNKNOWN, artifactId, context, "No conflict replacement was found");
            return;
        }
    }

}
