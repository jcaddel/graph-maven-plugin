/**
 * Copyright 2011-2012 The Kuali Foundation
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

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 * Nothing in this sanitizer takes into account the overall build tree. It only performs checks on individual nodes.
 * </p>
 *
 * <p>
 * Checks that nodes marked as <code>State.DUPLICATE</code> have 'related' artifacts identical to the main artifact
 * stored at that node. Also checks to make sure nodes marked as <code>State.CONFLICT</code> have a 'related' artifact
 * that is a different version from the main artifact stored at that node.
 * </p>
 *
 * <p>
 * Any <code>State.DUPLICATE</code> node that does not have an identical related artifact is switched to
 * <code>State.CONFLICT</code>
 * </p>
 *
 * <p>
 * Any <code>State.CONFLICT</code> node that has an identical related artifact is switched to
 * <code>State.DUPLICATE</code>
 * </p>
 *
 * @author jeffcaddel
 */
public class RelatedArtifactSanitizer implements NodeSanitizer<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(RelatedArtifactSanitizer.class);
    TreeHelper helper = new TreeHelper();

    @Override
    public void sanitize(Node<MavenContext> node) {
        // Extract anything Maven has marked as a duplicate or conflict
        List<MavenContext> contexts = helper.getList(node, DUPLICATE, CONFLICT);
        for (MavenContext context : contexts) {
            // Restore sanity
            sanitize(context);

        }
    }

    protected void sanitize(MavenContext context) {
        DependencyNode dn = context.getDependencyNode();
        // The validation logic assures that artifact and related are not null for conflicts and duplicates
        Artifact artifact = dn.getArtifact();
        Artifact related = dn.getRelatedArtifact();
        State state = State.getState(dn.getState());

        // Test them to see if they are exactly the same or just similar
        boolean equal = helper.equals(artifact, related);
        boolean similar = helper.similar(artifact, related);

        // The validation logic assures that one or the either is true, but no harm in double checking
        Assert.isTrue(equal || similar, "Invalid state.");

        if (equal) {
            // The main artifact and related artifact are exactly the same
            if (state == CONFLICT) {
                // Maven told us this was a CONFLICT, yet the artifacts are exactly the same.
                // WTF is up with that? The only thing it is possible to do at this point is
                // switch this artifact out WITH THE EXACT SAME ARTIFACT!!! How is that a conflict?
                logger.info(CONFLICT + "->" + DUPLICATE + " " + artifact);
            }
            // Set state to duplicate and make sure replacement is nulled out
            context.setState(DUPLICATE);
            context.setReplacement(null);
        } else if (similar) {
            // The main artifact and related artifact are NOT exactly the same, but they differ only by version
            if (state == DUPLICATE) {
                // Maven told us this was a DUPLICATE, yet the related artifact is a different version
                // We are going to assume the duplicate flag is a mistake and re-flag this as a conflict
                logger.info(DUPLICATE + "->" + CONFLICT + " " + artifact);
            }
            // Set state to conflict, and store the artifact Maven replaced it with
            context.setState(CONFLICT);
            context.setReplacement(related);
        } else {
            // Something has gone horribly wrong
            Assert.isTrue(false, "Invalid state");
        }
    }

}
