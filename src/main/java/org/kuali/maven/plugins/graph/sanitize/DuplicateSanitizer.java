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

import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicateSanitizer extends MavenContextSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateSanitizer.class);

    public DuplicateSanitizer() {
        this(null);
    }

    public DuplicateSanitizer(Map<String, MavenContext> included) {
        super(included, State.DUPLICATE);
    }

    @Override
    protected void sanitize(MavenContext context, Map<String, MavenContext> included) {
        String artifactId = TreeHelper.getArtifactId(context.getDependencyNode().getArtifact());
        MavenContext replacement = included.get(artifactId);

        // Nothing more to do
        if (replacement != null) {
            logger.debug(artifactId + " meets duplicate node criteria");
            return;
        }

        Artifact related = context.getDependencyNode().getRelatedArtifact();
        if (related == null) {
            // This is not ok. There was no exact match and related is null.
            warnAndSwitch(State.UNKNOWN, artifactId, context, "No related artifact");
            return;
        }

        String relatedArtifactId = TreeHelper.getArtifactId(related);
        replacement = included.get(relatedArtifactId);
        if (replacement != null) {
            /**
             * This is ok. Kind of. Maven has marked this as a duplicate because it's a duplicate of a dependency above
             * us in the tree. In reality, it is going to get switched out for another artifact because the node above
             * us has its dependency on this artifact marked as "conflict". The "duplicate" label is a little bit
             * counter intuitive but the overall build tree is in a consistent state.
             */
            // Emit an "info" level log message and switch to CONFLICT
            State switchTo = State.CONFLICT;
            logger.info(getSwitchMessage(artifactId, switchTo));
            logger.info("No identical replacement for a 'duplicate' but the related artifact was found");
            context.setState(switchTo);
            return;
        } else {
            // This is not ok. A node marked as duplicate has no replacement artifact included in the build
            warnAndSwitch(State.UNKNOWN, artifactId, context, "No identical replacement. Related artifact not found");
            return;
        }
    }

}
