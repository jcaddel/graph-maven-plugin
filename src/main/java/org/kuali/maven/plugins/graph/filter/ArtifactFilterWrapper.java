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
package org.kuali.maven.plugins.graph.filter;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extract an artifact from a MavenContext and use the wrapped artifact filter to filter it.
 */
public class ArtifactFilterWrapper implements NodeFilter<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(ArtifactFilterWrapper.class);

    Filter<Artifact> filter;

    public ArtifactFilterWrapper() {
        this(null);
    }

    public ArtifactFilterWrapper(Filter<Artifact> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public boolean isMatch(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        Artifact artifact = context.getArtifact();
        logger.debug("examining {}", artifact);
        return filter.isMatch(artifact);
    }

    public Filter<Artifact> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Artifact> filter) {
        this.filter = filter;
    }

}
