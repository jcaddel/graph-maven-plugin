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

public class DuplicateSanitizer extends MavenContextSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateSanitizer.class);

    public DuplicateSanitizer() {
        this(null);
    }

    public DuplicateSanitizer(Included included) {
        super(included, State.DUPLICATE);
    }

    @Override
    protected void sanitize(MavenContext context, Included included) {
        String id1 = context.getArtifactIdentifier();
        String partialId1 = context.getPartialArtifactIdentifier();
        Node<MavenContext> exact1 = included.getIds().get(id1);
        Node<MavenContext> partial1 = included.getPartialIds().get(partialId1);

        if (handlePrimary(context, exact1, partial1)) {
            return;
        }

        Artifact related = context.getDependencyNode().getRelatedArtifact();
        if (related == null) {
            // This is not ok. There was no exact match and related is null.
            warnAndSwitch(State.UNKNOWN, id1, context, "No replacement and no related artifact");
            return;
        }

        String id2 = TreeHelper.getArtifactId(related);
        String partialId2 = TreeHelper.getPartialArtifactId(related);
        Node<MavenContext> exact2 = included.getIds().get(id2);
        Node<MavenContext> partial2 = included.getPartialIds().get(partialId2);
        if (handleRelated(context, exact2, partial2)) {
            return;
        }
    }

    protected boolean handlePrimary(MavenContext context, Node<MavenContext> exact, Node<MavenContext> partial) {
        // Nothing more to do
        if (exact != null) {
            context.setReplacement(exact);
            logger.debug("{} meets duplicate node criteria", context.getArtifactIdentifier());
            return true;
        }

        /**
         * This is ok. Kind of. Maven has marked this as a duplicate because it's a duplicate of a dependency above us
         * in the tree. In reality, it is going to get switched out for another artifact because the node above us has
         * its dependency on this artifact marked as "conflict". The "duplicate" label is a little bit counter intuitive
         * but the overall build tree is in a consistent state.
         */
        if (partial != null) {
            switchState(context, partial);
            return true;
        }
        return false;
    }

    protected void switchState(MavenContext context, Node<MavenContext> replacement) {
        State switchTo = State.CONFLICT;
        logger.info(getSwitchMessage(context.getArtifactIdentifier(), switchTo));
        logger.info("No identical replacement for a 'duplicate' but a similar artifact was found");
        context.setState(switchTo);
        context.setReplacement(replacement);
    }

    protected boolean handleRelated(MavenContext context, Node<MavenContext> exact, Node<MavenContext> partial) {
        if (exact != null) {
            switchState(context, exact);
            return true;
        }

        if (partial != null) {
            switchState(context, partial);
            return true;
        }
        return false;
    }
}
