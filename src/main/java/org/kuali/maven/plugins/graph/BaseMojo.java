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
package org.kuali.maven.plugins.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.kuali.maven.plugins.graph.collector.DependencyNodeTokenCollector;
import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.kuali.maven.plugins.graph.dot.Dot;
import org.kuali.maven.plugins.graph.dot.GraphException;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.dot.StringGenerator;
import org.kuali.maven.plugins.graph.dot.edge.EdgeHandler;
import org.kuali.maven.plugins.graph.filter.ArtifactFilterWrapper;
import org.kuali.maven.plugins.graph.filter.DependencyNodeFilterWrapper;
import org.kuali.maven.plugins.graph.filter.DepthFilter;
import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.filter.Filters;
import org.kuali.maven.plugins.graph.filter.MatchCondition;
import org.kuali.maven.plugins.graph.filter.NodeFilter;
import org.kuali.maven.plugins.graph.filter.NodeFilterChain;
import org.kuali.maven.plugins.graph.filter.ReverseNodeFilter;
import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.DotContext;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.tree.TreeMetaData;

/**
 *
 */
@SuppressWarnings("deprecation")
public abstract class BaseMojo extends AbstractMojo {
    Filters filters = new Filters();

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
     * The title for the graph
     * </p>
     *
     * @parameter expression="${graph.title}" default-value="Dependency Graph for ${project.name}"
     */
    private String title;

    /**
     * <p>
     * Set to true to retain the .dot file used to draw the graph
     * </p>
     *
     * @parameter expression="${graph.keepDotFile}" default-value="false"
     */
    private boolean keepDotFile;

    /**
     * <p>
     * Set to false to show only the dependencies for the current project
     * </p>
     *
     * @parameter expression="${graph.transitive}" default-value="true"
     */
    private boolean transitive;

    /**
     * <p>
     * Comma delimited list of patterns for including artifacts.
     * </p>
     *
     * <p>
     * Include patterns work "bottom up" and are overridden by exclude patterns. If an artifact matches an include
     * pattern, it, and all of the dependencies in the path from it back to the root of the dependency tree are
     * displayed.
     * </p>
     *
     * <p>
     * If not provided all dependencies are included.
     * </p>
     *
     * <p>
     * The pattern syntax has the form - [groupId]:[artifactId]:[type]:[classifier]:[version]
     * </p>
     *
     * <p>
     * Each pattern segment is optional and supports <code>*</code> wildcards. An empty pattern segment is treated as a
     * wildcard.
     * </p>
     *
     * @parameter expression="${graph.includes}"
     */
    private String includes;

    /**
     * <p>
     * Comma delimited list of artifact patterns to exclude.
     * </p>
     *
     * <p>
     * Exclude patterns override include patterns and work "top down". If a dependency matches any exclude pattern, it,
     * and all dependencies below it, are removed from the display.
     * </p>
     *
     * <p>
     * If not provided, no artifacts are excluded.
     * </p>
     *
     * <p>
     * The pattern syntax has the form - [groupId]:[artifactId]:[type]:[classifier]:[version]
     * </p>
     *
     * <p>
     * Each pattern segment is optional and supports <code>*</code> wildcards. An empty pattern segment is treated as a
     * wildcard.
     * </p>
     *
     * @parameter expression="${graph.excludes}"
     */
    private String excludes;

    /**
     * <p>
     * Comma delimited list of dependency patterns used for hiding artifacts.
     * </p>
     *
     * <p>
     * Hide patterns override show patterns and work "top down". If a dependency matches any hide pattern, it, and all
     * dependencies below it, are removed from the display.
     * </p>
     *
     * <p>
     * If not provided, no dependencies are hidden.
     * </p>
     *
     * <p>
     * The pattern syntax has the form - [scope]:[optional|required]:[state]
     *
     * </p>
     *
     * <pre>
     * Scopes: compile,provided,runtime,test,system,import
     * States: normal,conflict,cyclic,duplicate
     * </pre>
     *
     * <p>
     * Each pattern segment is optional and supports <code>*</code> wildcards. An empty pattern segment is treated as a
     * wildcard.
     * </p>
     *
     * @parameter expression="${graph.hide}"
     */
    private String hide;

    /**
     * <p>
     * Comma delimited list of dependency patterns used for showing artifacts.
     * </p>
     *
     * <p>
     * Show patterns work "bottom up" and are overridden by hide patterns. If a dependency matches any show criteria,
     * it, and all of the dependencies in the direct path from it back to the root of the dependency tree are displayed.
     * </p>
     *
     * <p>
     * The pattern syntax has the form - [scope]:[optional|required]:[state]
     * </p>
     *
     * <pre>
     * Scopes: compile,provided,runtime,test,system,import
     * States: normal,conflict,cyclic,duplicate
     * </pre>
     *
     * <p>
     * Each pattern segment is optional and supports <code>*</code> wildcards. An empty pattern segment is treated as a
     * wildcard.
     * </p>
     *
     * @parameter expression="${graph.show}"
     */
    private String show;

    /**
     * <p>
     * The direction for the graph layout. Top to bottom, left to right, bottom to top, and right to left.
     * </p>
     *
     * @parameter expression="${graph.direction}" default-value="TB"
     */
    private Direction direction;

    @Override
    public void execute() {
        try {
            String content = getDotFileContent(title, direction);
            Dot dot = new Dot();
            DotContext context = dot.getDotContext(getFile(), content, keepDotFile);
            dot.execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract File getFile();

    protected abstract EdgeHandler getEdgeHandler();

    /**
     * Restricts the depth of the dependency tree. To show only the dependencies of your project, set this to 1. To show
     * the dependencies of your project and their direct dependencies, set this to 2.
     *
     * @parameter expression="${graph.depth}" default-value="-1"
     */
    private int depth;

    protected DepthFilter<MavenContext> getDepthFilter() {
        int maxDepth = transitive ? DepthFilter.INFINITE : 1;
        maxDepth = depth >= 0 ? depth : maxDepth;
        return new DepthFilter<MavenContext>(maxDepth);
    }

    protected void preProcess(Node<MavenContext> node) {
        // do nothing by default
    }

    protected void postProcess(Node<MavenContext> node, List<GraphNode> nodes, List<Edge> edges) {
        // do nothing by default
    }

    protected String getDotFileContent(String title, Direction direction) {
        TreeHelper helper = new TreeHelper();
        DependencyNode mavenTree = getMavenTree();
        Node<MavenContext> nodeTree = helper.getTree(mavenTree);
        preProcess(nodeTree);
        helper.sanitize(nodeTree);
        TreeMetaData md = helper.getMetaData(nodeTree);
        helper.show(md);
        helper.include(nodeTree, getIncludeFilter());
        helper.exclude(nodeTree, getExcludeFilter());
        List<GraphNode> nodes = helper.getGraphNodes(nodeTree);
        EdgeHandler handler = getEdgeHandler();
        List<Edge> edges = helper.getEdges(nodeTree, handler);
        postProcess(nodeTree, nodes, edges);
        helper.show(nodes, edges);
        Graph graph = new GraphHelper().getGraph(title, direction, nodes, edges);
        return new StringGenerator().getString(graph);
    }

    protected NodeFilter<MavenContext> getShowFilter() {
        TokenCollector<DependencyNode> collector = new DependencyNodeTokenCollector();
        Filter<DependencyNode> filter = filters.getIncludePatternFilter(getShow(), collector);
        return new DependencyNodeFilterWrapper(filter);
    }

    protected NodeFilter<MavenContext> getHideFilter() {
        TokenCollector<DependencyNode> collector = new DependencyNodeTokenCollector();
        Filter<DependencyNode> filter = filters.getExcludePatternFilter(getHide(), collector);
        return new DependencyNodeFilterWrapper(filter);
    }

    protected NodeFilter<MavenContext> getIncludeFilter() {
        TokenCollector<Artifact> collector = new org.kuali.maven.plugins.graph.collector.ArtifactIdTokenCollector();
        Filter<Artifact> filter = filters.getIncludePatternFilter(getIncludes(), collector);
        ArtifactFilterWrapper artifactFilter = new ArtifactFilterWrapper(filter);
        List<NodeFilter<MavenContext>> filters = new ArrayList<NodeFilter<MavenContext>>();
        NodeFilter<MavenContext> artifactQualifierFilter = getShowFilter();
        filters.add(artifactQualifierFilter);
        filters.add(artifactFilter);
        return new NodeFilterChain<MavenContext>(filters, MatchCondition.ALL, true);
    }

    protected NodeFilter<MavenContext> getExcludeFilter() {
        TokenCollector<Artifact> collector = new org.kuali.maven.plugins.graph.collector.ArtifactIdTokenCollector();
        Filter<Artifact> filter = filters.getExcludePatternFilter(getExcludes(), collector);
        ArtifactFilterWrapper artifactFilter = new ArtifactFilterWrapper(filter);
        ReverseNodeFilter<MavenContext> depthFilter = new ReverseNodeFilter<MavenContext>(getDepthFilter());
        NodeFilter<MavenContext> artifactQualifierFilter = getHideFilter();
        List<NodeFilter<MavenContext>> filters = new ArrayList<NodeFilter<MavenContext>>();
        filters.add(artifactQualifierFilter);
        filters.add(artifactFilter);
        filters.add(depthFilter);
        return new NodeFilterChain<MavenContext>(filters, MatchCondition.ANY, false);
    }

    protected DependencyNode getMavenTree() {
        try {
            return getTreeBuilder().buildDependencyTree(project, localRepository, artifactFactory,
                    artifactMetadataSource, null, artifactCollector);
        } catch (DependencyTreeBuilderException e) {
            throw new GraphException(e);
        }
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isKeepDotFile() {
        return keepDotFile;
    }

    public void setKeepDotFile(boolean keepDotFile) {
        this.keepDotFile = keepDotFile;
    }

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public MavenProject getProject() {
        return project;
    }

    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    public ArtifactResolver getArtifactResolver() {
        return artifactResolver;
    }

    public ArtifactFactory getArtifactFactory() {
        return artifactFactory;
    }

    public ArtifactMetadataSource getArtifactMetadataSource() {
        return artifactMetadataSource;
    }

    public ArtifactCollector getArtifactCollector() {
        return artifactCollector;
    }

    public DependencyTreeBuilder getTreeBuilder() {
        return treeBuilder;
    }

}