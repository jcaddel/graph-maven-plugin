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
package org.kuali.maven.plugins.graph.collector;

import java.util.List;

import org.apache.maven.artifact.Artifact;

/**
 * <p>
 * Provide tokens that uniquely identify a Maven Artifact
 * </p>
 *
 * <pre>
 * [groupId]:[artifactId]:[type]:[classifier]:[version]
 * </pre>
 */
public class ArtifactIdTokenCollector extends VersionFreeArtifactTokenCollector {

    /**
     * [groupId]:[artifactId]:[type]:[classifier]:[version]
     */
    @Override
    public List<String> getTokens(Artifact artifact) {
        List<String> tokens = super.getTokens(artifact);
        tokens.add(artifact.getVersion());
        return tokens;
    }

}
