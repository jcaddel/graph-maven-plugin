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
        tokens.add(artifact.getBaseVersion());
        return tokens;
    }

}
