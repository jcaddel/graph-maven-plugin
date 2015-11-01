/**
 * Copyright 2011-2013 The Kuali Foundation
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

import org.kuali.maven.plugins.graph.dot.EdgeGenerator;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;

public class FlatEdgeProcessor implements Processor {
    EdgeGenerator generator;

    public FlatEdgeProcessor(GraphDescriptor gd) {
        generator = new EdgeGenerator(gd);
    }

    @Override
    public void process(Node<MavenContext> node) {
        for (Node<MavenContext> child : node.getChildren()) {
            // Create an edge running from the child's parent to itself
            Edge edge = generator.getParentChildEdge(child);
            // Add the edge to the parent's list
            generator.addEdge(child.getParent(), edge);
            // Continue processing the tree
            process(child);
        }
    }

}
