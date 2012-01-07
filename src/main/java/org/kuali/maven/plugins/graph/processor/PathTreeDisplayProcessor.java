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

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.mojo.MojoHelper;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class PathTreeDisplayProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(PathTreeDisplayProcessor.class);
    MojoHelper mh = new MojoHelper();
    TreeHelper helper = new TreeHelper();
    GraphDescriptor graphDescriptor;

    public PathTreeDisplayProcessor() {
        this(null);
    }

    public PathTreeDisplayProcessor(GraphDescriptor graphDescriptor) {
        super();
        this.graphDescriptor = graphDescriptor;
    }

    @Override
    public void process(Node<MavenContext> node) {
        Filter<Node<MavenContext>> filter = mh.getIncludeExcludeFilter(graphDescriptor);
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        List<Node<MavenContext>> displayed = new ArrayList<Node<MavenContext>>();
        for (Node<MavenContext> element : list) {
            if (!element.isRoot()) {
                helper.hide(element);
            }
            boolean display = filter.isMatch(element);
            if (display) {
                displayed.add(element);
                logger.info("displaying " + element.getObject().getArtifactIdentifier());
            }
        }
        for (Node<MavenContext> element : displayed) {
            helper.showPath(element);
            helper.showTree(element);
            showDuplicates(element);
        }

    }

    protected void showDuplicates(Node<MavenContext> node) {
        for (Node<MavenContext> child : node.getChildren()) {
            boolean correctState = State.DUPLICATE == child.getObject().getState();
            if (correctState) {
                String artifactId = child.getObject().getArtifactIdentifier();
                Node<MavenContext> included = TreeHelper.findRequiredIncludedNode(node.getRoot(), artifactId);
                helper.showTree(included);
                helper.showPath(included);
                showDuplicates(child);
            }
        }
    }

    public GraphDescriptor getGraphDescriptor() {
        return graphDescriptor;
    }

    public void setGraphDescriptor(GraphDescriptor graphDescriptor) {
        this.graphDescriptor = graphDescriptor;
    }

}
