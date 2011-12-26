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
package org.kuali.maven.plugins.graph.mojo;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;

/**
 * <p>
 * Abstraction for mojo's that produce graphs using Graphviz
 * </p>
 *
 */
@SuppressWarnings("deprecation")
public abstract class BaseMavenMojo extends AbstractMojo {

    /**
     * @required
     * @readonly
     * @parameter expression="${project}"
     * @since 1.0
     */
    private MavenProject project;

    /**
     * @required
     * @readonly
     * @parameter expression="${localRepository}"
     * @since 1.0
     */
    private ArtifactRepository localRepository;

    /**
     * @required
     * @component
     * @since 1.0
     */
    private ArtifactResolver artifactResolver;

    /**
     * @required
     * @readonly
     * @component
     * @since 1.0
     */
    private ArtifactFactory artifactFactory;

    /**
     * @required
     * @readonly
     * @component
     * @since 1.0
     */
    private ArtifactMetadataSource artifactMetadataSource;

    /**
     * @required
     * @readonly
     * @component
     */
    private ArtifactCollector artifactCollector;

    /**
     * @required
     * @readonly
     * @component
     * @since 1.0
     */
    private DependencyTreeBuilder treeBuilder;

    /**
     * <p>
     * If true, mojo execution is skipped.
     * </p>
     *
     * @parameter expression="${graph.skip}" default-value="false"
     */
    private boolean skip;

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    public void setLocalRepository(ArtifactRepository localRepository) {
        this.localRepository = localRepository;
    }

    public ArtifactResolver getArtifactResolver() {
        return artifactResolver;
    }

    public void setArtifactResolver(ArtifactResolver artifactResolver) {
        this.artifactResolver = artifactResolver;
    }

    public ArtifactFactory getArtifactFactory() {
        return artifactFactory;
    }

    public void setArtifactFactory(ArtifactFactory artifactFactory) {
        this.artifactFactory = artifactFactory;
    }

    public ArtifactMetadataSource getArtifactMetadataSource() {
        return artifactMetadataSource;
    }

    public void setArtifactMetadataSource(ArtifactMetadataSource artifactMetadataSource) {
        this.artifactMetadataSource = artifactMetadataSource;
    }

    public ArtifactCollector getArtifactCollector() {
        return artifactCollector;
    }

    public void setArtifactCollector(ArtifactCollector artifactCollector) {
        this.artifactCollector = artifactCollector;
    }

    public DependencyTreeBuilder getTreeBuilder() {
        return treeBuilder;
    }

    public void setTreeBuilder(DependencyTreeBuilder treeBuilder) {
        this.treeBuilder = treeBuilder;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

}