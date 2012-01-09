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
package org.kuali.maven.plugins.graph.processor;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;

public class LabelProcessor implements Processor {
    GraphDescriptor graphDescriptor;

    public LabelProcessor() {
        this(null);
    }

    public LabelProcessor(GraphDescriptor graphDescriptor) {
        super();
        this.graphDescriptor = graphDescriptor;
    }

    @Override
    public void process(Node<MavenContext> node) {
        GraphHelper graphHelper = new GraphHelper();
        List<Node<MavenContext>> treeNodes = node.getBreadthFirstList();
        for (Node<MavenContext> element : treeNodes) {
            MavenContext mc = element.getObject();
            GraphNode graphNode = mc.getGraphNode();
            Artifact artifact = mc.getArtifact();
            String label = graphHelper.getLabel(artifact, graphDescriptor);
            graphNode.setLabel(label);
        }
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

}
