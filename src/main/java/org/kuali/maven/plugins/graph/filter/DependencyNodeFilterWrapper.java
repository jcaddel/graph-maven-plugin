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
package org.kuali.maven.plugins.graph.filter;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyNodeFilterWrapper implements NodeFilter<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(DependencyNodeFilterWrapper.class);

    Filter<DependencyNode> filter;

    public DependencyNodeFilterWrapper() {
        this(null);
    }

    public DependencyNodeFilterWrapper(Filter<DependencyNode> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public boolean isMatch(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        DependencyNode dependencyNode = context.getDependencyNode();
        boolean match = filter.isMatch(dependencyNode);
        logger.debug("match={} for {}", match, dependencyNode.getArtifact());
        return match;
    }

    public Filter<DependencyNode> getFilter() {
        return filter;
    }

    public void setFilter(Filter<DependencyNode> filter) {
        this.filter = filter;
    }

}
