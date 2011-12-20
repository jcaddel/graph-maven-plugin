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

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyclicSanitizer extends MavenContextSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(CyclicSanitizer.class);

    public CyclicSanitizer() {
        this(null);
    }

    public CyclicSanitizer(Map<String, MavenContext> included) {
        super(included, State.CYCLIC);
    }

    @Override
    protected void sanitize(MavenContext context, Map<String, MavenContext> included) {
        // Anything we need to do here?
        String id = TreeHelper.getArtifactId(context.getDependencyNode().getArtifact());
        logger.warn("cyclic->" + id);
    }

}
