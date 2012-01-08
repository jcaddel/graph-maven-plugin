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
package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.kuali.maven.plugins.graph.collector.ArtifactIdTokenCollector;
import org.kuali.maven.plugins.graph.collector.MavenContextTokenCollector;
import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.kuali.maven.plugins.graph.dot.Dot;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.dot.StringGenerator;
import org.kuali.maven.plugins.graph.filter.ArtifactFilterWrapper;
import org.kuali.maven.plugins.graph.filter.DepthFilter;
import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.filter.Filters;
import org.kuali.maven.plugins.graph.filter.IncludeExcludeFilter;
import org.kuali.maven.plugins.graph.filter.MatchCondition;
import org.kuali.maven.plugins.graph.filter.MavenContextFilterWrapper;
import org.kuali.maven.plugins.graph.filter.NodeFilter;
import org.kuali.maven.plugins.graph.filter.NodeFilterChain;
import org.kuali.maven.plugins.graph.filter.ReverseNodeFilter;
import org.kuali.maven.plugins.graph.pojo.Category;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.Layout;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.pojo.Row;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.processor.CascadeOptionalProcessor;
import org.kuali.maven.plugins.graph.processor.FlatEdgeProcessor;
import org.kuali.maven.plugins.graph.processor.HideConflictsProcessor;
import org.kuali.maven.plugins.graph.processor.HideDuplicatesProcessor;
import org.kuali.maven.plugins.graph.processor.LabelProcessor;
import org.kuali.maven.plugins.graph.processor.LinkedEdgeProcessor;
import org.kuali.maven.plugins.graph.processor.PathDisplayProcessor;
import org.kuali.maven.plugins.graph.processor.PathTreeDisplayProcessor;
import org.kuali.maven.plugins.graph.processor.Processor;
import org.kuali.maven.plugins.graph.processor.ReduceClutterProcessor;
import org.kuali.maven.plugins.graph.processor.SanitizingProcessor;
import org.kuali.maven.plugins.graph.processor.ShowMetadataProcessor;
import org.kuali.maven.plugins.graph.processor.StyleProcessor;
import org.kuali.maven.plugins.graph.processor.TreeDisplayProcessor;
import org.kuali.maven.plugins.graph.processor.ValidatingProcessor;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.util.Counter;
import org.kuali.maven.plugins.graph.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MojoHelper {
    private static final Logger logger = LoggerFactory.getLogger(MojoHelper.class);
    Filters filters = new Filters();

    public Filter<Node<MavenContext>> getIncludeExcludeFilter(GraphDescriptor descriptor) {
        NodeFilter<MavenContext> include = getIncludeFilter(descriptor);
        NodeFilter<MavenContext> exclude = getExcludeFilter(descriptor);
        return new IncludeExcludeFilter<Node<MavenContext>>(include, exclude);
    }

    public void categories(MojoContext mc, GraphDescriptor gc, List<Category> categories) {
        if (mc.isSkip()) {
            logger.info("Skipping execution");
            return;
        }
        if (Helper.isEmpty(categories) && !mc.isGenerateDefaultGraphs()) {
            logger.info("No categories");
            return;
        }
        if (mc.isGenerateDefaultGraphs()) {
            categories.addAll(0, getDefaultCategories(gc));
        }
        int count = 0;
        for (Category category : categories) {
            for (Row row : category.getRows()) {
                row.setCategory(category);
                fillInDescriptors(gc, row.getDescriptors(), mc.getOutputDir(), row);
                List<GraphDescriptor> executed = execute(mc, gc, row.getDescriptors());
                count += executed.size();
                row.setDescriptors(executed);
            }
        }
        if (count == 0) {
            logger.info("No graphs to generate");
        }
    }

    public List<GraphDescriptor> executeMulti(MojoContext mc, GraphDescriptor gd, List<GraphDescriptor> descriptors) {
        try {
            if (mc.isSkip()) {
                logger.info("Skipping execution");
                return null;
            }
            if (Helper.isEmpty(descriptors) && !mc.isGenerateDefaultGraphs()) {
                logger.info("No descriptors");
                return null;
            }
            if (mc.isGenerateDefaultGraphs()) {
                descriptors.addAll(0, getDefaultDescriptors(mc, gd));
            }
            fillInDescriptors(gd, descriptors, mc.getOutputDir(), null);
            List<GraphDescriptor> executedGraphs = new ArrayList<GraphDescriptor>();
            for (GraphDescriptor descriptor : descriptors) {
                GraphDescriptor executed = execute(mc, descriptor);
                if (executed != null) {
                    executedGraphs.add(executed);
                }
            }
            return executedGraphs;
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    public List<GraphDescriptor> execute(MojoContext mc, GraphDescriptor gd, List<GraphDescriptor> descriptors) {
        try {
            if (mc.isSkip()) {
                logger.info("Skipping execution");
                return null;
            }
            if (Helper.isEmpty(descriptors)) {
                logger.info("No descriptors");
                return null;
            }
            List<GraphDescriptor> executedGraphs = new ArrayList<GraphDescriptor>();
            for (GraphDescriptor descriptor : descriptors) {
                GraphDescriptor executed = execute(mc, descriptor);
                if (executed != null) {
                    executedGraphs.add(executed);
                }
            }
            return executedGraphs;
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    protected void fillInDescriptors(GraphDescriptor gd, List<GraphDescriptor> gds, File outputDir, Row row) {
        gd.setRow(row);
        logger.debug("default output format={}", gd.getOutputFormat());
        Counter counter = new Counter(1);
        for (GraphDescriptor desc : gds) {
            Helper.copyPropertiesIfNull(desc, gd);
            if (desc.getName() == null) {
                desc.setName(counter.increment() + "");
            }
            if (desc.getTransitive() == null) {
                desc.setTransitive(true);
            }
            if (desc.getLayout() == null) {
                desc.setLayout(Layout.LINKED);
            }
            String filename = getFilename(desc);
            String path = desc.getPath();
            if (path == null && row != null) {
                path = getPathFromRow(desc.getRow());
            }
            File file = new File(outputDir, Helper.toEmpty(path) + "/" + filename);
            desc.setFile(file);
        }
    }

    protected List<GraphDescriptor> getDefaultDescriptors(MojoContext mc, GraphDescriptor gd) {
        List<GraphDescriptor> descriptors = new ArrayList<GraphDescriptor>();
        List<Category> categories = getDefaultCategories(gd);
        for (Category category : categories) {
            for (Row row : category.getRows()) {
                descriptors.addAll(row.getDescriptors());
                fillInDescriptors(gd, row.getDescriptors(), mc.getOutputDir(), row);
            }
        }
        return descriptors;
    }

    protected List<Category> getDefaultCategories(GraphDescriptor gd) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(getCategory(gd, false));
        categories.add(getCategory(gd, true));
        return categories;
    }

    protected String getPathFromRow(Row row) {
        StringBuilder sb = new StringBuilder();
        sb.append(row.getCategory().getName());
        sb.append("/");
        sb.append(row.getName());
        return sb.toString();
    }

    protected String getFilename(GraphDescriptor gd) {
        StringBuilder sb = new StringBuilder();
        sb.append(gd.getName());
        sb.append(".");
        sb.append(gd.getOutputFormat());
        return sb.toString();
    }

    protected String getRelativeFilename(GraphDescriptor gd) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPathFromRow(gd.getRow()));
        sb.append("/");
        sb.append(getFilename(gd));
        return sb.toString();
    }

    protected Category getCategory(GraphDescriptor gd, boolean transitive) {
        String name = getTransitiveLabel(transitive);
        Category c = new Category(name);
        c.setDescription(getDescription(transitive));
        c.setRows(getRows(gd, transitive, c));
        return c;
    }

    protected String getTransitiveLabel(boolean transitive) {
        return transitive ? "transitive" : "direct";
    }

    protected String getDescription(Scope scope) {
        if (scope == null) {
            return "These are the dependencies of the project for all scopes.";
        }
        switch (scope) {
        case COMPILE:
            return "These dependencies are required for compilation.  They are available in all classpaths of the project and are also propagated as transitive dependencies to projects that depend on this project";
        case IMPORT:
            return "This scope is only used on a dependency of type pom in the <dependencyManagement> section. It indicates that the specified POM should be replaced with the dependencies in that POM's <dependencyManagement> section";
        case PROVIDED:
            return "Similar to compile, but with the expectation that the JDK or a container will provide the dependency at runtime.  These dependencies are only available on the compilation and test classpaths, and are not transitive.";
        case RUNTIME:
            return "These dependencies are not required for compilation, but are for execution. They are in the runtime classpath (eg WEB-INF/lib) and test classpath, but not the compile classpath.";
        case SYSTEM:
            return "This scope is similar to provided except that you have to provide the JAR which contains it explicitly. The artifact must always be available and is not looked up in a repository.";
        case TEST:
            return "These dependencies are only required to compile and run unit tests for the application";
        default:
            throw new IllegalArgumentException("Unknown scope " + scope);
        }
    }

    protected String getDescription(boolean transitive) {
        if (transitive) {
            return "These are the dependencies declared in this project's pom plus any transitive dependencies of those dependencies.";
        } else {
            return "These are the dependencies declared in this project's pom";
        }
    }

    protected String getScopeLabel(Scope scope) {
        return scope == null ? "all" : scope.toString();
    }

    protected List<Row> getRows(GraphDescriptor gd, boolean transitive, Category category) {
        List<Row> rows = new ArrayList<Row>();
        Row any = new Row(getScopeLabel(null));
        any.setCategory(category);
        any.setDescriptors(getDescriptors(gd, any, transitive, null));
        any.setDescription(getDescription(null));
        rows.add(any);
        for (Scope scope : Scope.values()) {
            Row row = new Row(getScopeLabel(scope));
            row.setCategory(category);
            row.setDescription(getDescription(scope));
            row.setDescriptors(getDescriptors(gd, row, transitive, scope));
            rows.add(row);
        }
        return rows;
    }

    protected List<GraphDescriptor> getDescriptors(GraphDescriptor gd, Row row, boolean transitive, Scope scope) {
        List<GraphDescriptor> descriptors = new ArrayList<GraphDescriptor>();
        for (Layout layout : Layout.values()) {
            descriptors.add(getDescriptor(gd, row, transitive, scope, layout));
        }
        return descriptors;
    }

    protected GraphDescriptor getDescriptor(GraphDescriptor gd, Row row, boolean transitive, Scope scope, Layout layout) {
        GraphDescriptor descriptor = Helper.copyProperties(GraphDescriptor.class, gd);
        descriptor.setShow(scope == null ? null : scope.toString());
        descriptor.setTransitive(transitive);
        descriptor.setName(layout.toString().toLowerCase());
        descriptor.setLayout(layout);
        descriptor.setRow(row);
        descriptor.setPath(getPathFromRow(row));
        return descriptor;
    }

    public GraphDescriptor execute(MojoContext mc, GraphDescriptor gc) {
        if (mc.isSkip()) {
            logger.info("Skipping execution");
            return null;
        }

        try {
            Node<MavenContext> tree = getProcessedTree(mc, gc);
            Graph graph = getGraph(tree, mc, gc);
            if (isEmptyGraph(graph) && Boolean.TRUE.equals(gc.getSkipEmptyGraphs())) {
                logger.debug("Skipping empty graph");
                return null;
            }
            String content = getDotFileContent(graph);
            Dot dot = new Dot();
            dot.fillInContext(gc, content);
            int exitValue = dot.execute(gc);
            if (exitValue == Dot.SUCCESS) {
                logger.info(gc.getFile().getPath());
                return gc;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    protected boolean isEmptyGraph(Graph graph) {
        int count = 0;
        List<GraphNode> nodes = graph.getNodes();
        for (GraphNode node : nodes) {
            if (!node.isHidden()) {
                count++;
            }
        }
        return count <= 1;
    }

    protected String getDotFileContent(Graph graph) {
        return new StringGenerator().getString(graph);
    }

    public DependencyNode getMavenTree(MojoContext c) {
        try {
            DependencyTreeBuilder builder = c.getTreeBuilder();
            return builder.buildDependencyTree(c.getProject(), c.getLocalRepository(), c.getArtifactFactory(),
                    c.getArtifactMetadataSource(), null, c.getArtifactCollector());
        } catch (DependencyTreeBuilderException e) {
            throw new GraphException(e);
        }
    }

    protected List<Processor> getProcessors(GraphDescriptor gc, boolean verbose) {
        List<Processor> processors = new ArrayList<Processor>();

        // Generate node labels
        processors.add(new LabelProcessor(gc));

        // Show some metadata
        if (verbose) {
            processors.add(new ShowMetadataProcessor());
        }

        // Figure out what nodes we are going to display
        processors.add(getDisplayProcessor(gc));

        // Style the nodes based on scope, optional/required, and state
        processors.add(new StyleProcessor());

        // Generate lines connecting the tree nodes
        processors.addAll(getEdgeProcessors(gc.getLayout()));

        // Hide duplicates
        if (!Boolean.TRUE.equals(gc.getShowDuplicates())) {
            processors.add(new HideDuplicatesProcessor());
        }

        // Hide conflicts
        if (!Boolean.TRUE.equals(gc.getShowConflicts())) {
            processors.add(new HideConflictsProcessor(gc.getLayout()));
        }

        return processors;
    }

    protected Processor getDisplayProcessor(GraphDescriptor gd) {
        switch (gd.getDisplay()) {
        case PATH:
            return new PathDisplayProcessor(gd, false);
        case TREE:
            return new TreeDisplayProcessor(gd);
        case PT: // Path and Tree
            return new PathTreeDisplayProcessor(gd);
        default:
            throw new IllegalStateException("Unknown filter type " + gd.getDisplay());
        }

    }

    protected Node<MavenContext> getProcessedTree(MojoContext mc, GraphDescriptor gc) {
        TreeHelper helper = new TreeHelper();
        if (mc.getMavenTree() == null) {
            DependencyNode mavenTree = getMavenTree(mc);
            mc.setMavenTree(mavenTree);
        }
        if (mc.getSanitizedTree() == null) {
            // The sanitized tree has all the funky conflict/duplicate stuff sorted out,
            // but has no edges or styling except for the root node being gray
            Node<MavenContext> tree = helper.getTree(mc.getMavenTree());
            sanitizeTree(tree, gc);
            mc.setSanitizedTree(tree);

        }
        Node<MavenContext> copy = helper.copy(mc.getSanitizedTree());
        List<Processor> processors = getProcessors(gc, mc.isVerbose());
        for (Processor processor : processors) {
            logger.debug("processor={}", processor.getClass());
            processor.process(copy);
        }
        return copy;
    }

    protected void sanitizeTree(Node<MavenContext> node, GraphDescriptor gd) {
        // Validate some basic things about the tree Maven gave us
        new ValidatingProcessor().process(node);

        // Clean up a few funky edge cases
        // Maven gives us a tree where CONFLICT nodes have identical artifacts and DUPLICATE nodes have
        // different artifacts. Also, DUPLICATE and CONFLICT nodes sometimes don't have a match with an artifact in the
        // INCLUDED nodes
        new SanitizingProcessor().process(node);

        // Cascade Maven's optional flag to transitive dependencies of optional dependencies
        if (Boolean.TRUE.equals(gd.getCascadeOptional())) {
            new CascadeOptionalProcessor().process(node);
        }
    }

    public Graph getGraph(Node<MavenContext> tree, MojoContext mc, GraphDescriptor gc) {
        GraphHelper gh = new GraphHelper();
        TreeHelper helper = new TreeHelper();
        List<GraphNode> nodes = helper.getGraphNodes(tree);
        List<Edge> edges = helper.getEdges(tree);
        if (mc.isVerbose()) {
            helper.show(nodes, edges);
        }
        String title = gh.getGraphTitle(gc);
        return gh.getGraph(title, gc.getDirection(), nodes, edges);
    }

    protected List<? extends Processor> getEdgeProcessors(Layout layout) {
        switch (layout) {
        case LINKED:
            List<Processor> processors = new ArrayList<Processor>();
            processors.add(new LinkedEdgeProcessor());
            processors.add(new ReduceClutterProcessor());
            return processors;
        case FLAT:
            return Collections.singletonList(new FlatEdgeProcessor());
        default:
            throw new IllegalStateException("Layout style " + layout + " is unknown");
        }
    }

    protected NodeFilter<MavenContext> getShowFilter(GraphDescriptor gc) {
        TokenCollector<MavenContext> collector = new MavenContextTokenCollector();
        Filter<MavenContext> filter = filters.getIncludePatternFilter(gc.getShow(), collector);
        return new MavenContextFilterWrapper(filter);
    }

    protected NodeFilter<MavenContext> getHideFilter(GraphDescriptor gc) {
        TokenCollector<MavenContext> collector = new MavenContextTokenCollector();
        Filter<MavenContext> filter = filters.getExcludePatternFilter(gc.getHide(), collector);
        return new MavenContextFilterWrapper(filter);
    }

    public NodeFilter<MavenContext> getIncludeFilter(GraphDescriptor gc) {
        TokenCollector<Artifact> collector = new ArtifactIdTokenCollector();
        Filter<Artifact> filter = filters.getIncludePatternFilter(gc.getIncludes(), collector);
        ArtifactFilterWrapper artifactFilter = new ArtifactFilterWrapper(filter);
        List<NodeFilter<MavenContext>> filters = new ArrayList<NodeFilter<MavenContext>>();
        NodeFilter<MavenContext> artifactQualifierFilter = getShowFilter(gc);
        filters.add(artifactQualifierFilter);
        filters.add(artifactFilter);
        return new NodeFilterChain<MavenContext>(filters, MatchCondition.ALL, true);
    }

    public NodeFilter<MavenContext> getExcludeFilter(GraphDescriptor gc) {
        TokenCollector<Artifact> collector = new ArtifactIdTokenCollector();
        Filter<Artifact> filter = filters.getExcludePatternFilter(gc.getExcludes(), collector);
        ArtifactFilterWrapper artifactFilter = new ArtifactFilterWrapper(filter);
        ReverseNodeFilter<MavenContext> depthFilter = new ReverseNodeFilter<MavenContext>(getDepthFilter(gc));
        NodeFilter<MavenContext> artifactQualifierFilter = getHideFilter(gc);
        List<NodeFilter<MavenContext>> filters = new ArrayList<NodeFilter<MavenContext>>();
        filters.add(artifactQualifierFilter);
        filters.add(artifactFilter);
        filters.add(depthFilter);
        return new NodeFilterChain<MavenContext>(filters, MatchCondition.ANY, false);
    }

    protected DepthFilter<MavenContext> getDepthFilter(GraphDescriptor gc) {
        int maxDepth = gc.getTransitive() ? DepthFilter.INFINITE : 1;
        maxDepth = (gc.getDepth() != null && gc.getDepth() >= 0) ? gc.getDepth() : maxDepth;
        return new DepthFilter<MavenContext>(maxDepth);
    }

}
