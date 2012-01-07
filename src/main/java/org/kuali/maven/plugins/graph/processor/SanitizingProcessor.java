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
package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.sanitize.BuildSanitizer;
import org.kuali.maven.plugins.graph.sanitize.NodeSanitizer;
import org.kuali.maven.plugins.graph.sanitize.RelatedArtifactSanitizer;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Sanitize the dependency tree.
 * </p>
 *
 */
public class SanitizingProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(SanitizingProcessor.class);
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> node) {
        // Flatten the tree into a list
        List<Node<MavenContext>> nodes = node.getBreadthFirstList();

        int included = helper.getList(node, State.INCLUDED).size();

        logger.debug("Sanitizing metadata for " + nodes.size() + " dependency nodes (" + included
                + " unique artifacts in the build)");

        // Go through the tree and clean up nodes that are not included in the build
        List<NodeSanitizer<MavenContext>> sanitizers = getSanitizers(node);
        for (NodeSanitizer<MavenContext> sanitizer : sanitizers) {
            sanitizer.sanitize(node);
        }
    }

    protected List<NodeSanitizer<MavenContext>> getSanitizers(Node<MavenContext> node) {
        List<NodeSanitizer<MavenContext>> sanitizers = new ArrayList<NodeSanitizer<MavenContext>>();
        sanitizers.add(new RelatedArtifactSanitizer());
        sanitizers.add(new BuildSanitizer());
        return sanitizers;
    }

}
