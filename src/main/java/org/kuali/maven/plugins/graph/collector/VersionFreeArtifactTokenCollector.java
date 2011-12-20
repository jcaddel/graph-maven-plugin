package org.kuali.maven.plugins.graph.collector;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;

/**
 * <p>
 * Provide tokens that identify a Maven Artifact sans version
 * </p>
 *
 * <pre>
 * [groupId]:[artifactId]:[type]:[classifier]
 * </pre>
 */
public class VersionFreeArtifactTokenCollector implements TokenCollector<Artifact> {

    /**
     * [groupId]:[artifactId]:[type]:[classifier]
     */
    @Override
    public List<String> getTokens(Artifact artifact) {
        List<String> tokens = new ArrayList<String>();
        tokens.add(artifact.getGroupId());
        tokens.add(artifact.getArtifactId());
        tokens.add(artifact.getType());
        tokens.add(artifact.getClassifier());
        return tokens;
    }

}
