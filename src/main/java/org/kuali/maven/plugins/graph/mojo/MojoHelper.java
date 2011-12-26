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
import org.kuali.maven.plugins.graph.dot.CondensedEdgeHandler;
import org.kuali.maven.plugins.graph.dot.Dot;
import org.kuali.maven.plugins.graph.dot.EdgeHandler;
import org.kuali.maven.plugins.graph.dot.FlatEdgeHandler;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.dot.StringGenerator;
import org.kuali.maven.plugins.graph.filter.ArtifactFilterWrapper;
import org.kuali.maven.plugins.graph.filter.DepthFilter;
import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.filter.Filters;
import org.kuali.maven.plugins.graph.filter.MatchCondition;
import org.kuali.maven.plugins.graph.filter.MavenContextFilterWrapper;
import org.kuali.maven.plugins.graph.filter.NodeFilter;
import org.kuali.maven.plugins.graph.filter.NodeFilterChain;
import org.kuali.maven.plugins.graph.filter.ReverseNodeFilter;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphContext;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.LayoutStyle;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.tree.ConflictsProcessor;
import org.kuali.maven.plugins.graph.tree.Counter;
import org.kuali.maven.plugins.graph.tree.Helper;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.PostProcessor;
import org.kuali.maven.plugins.graph.tree.PreProcessor;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.tree.TreeMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MojoHelper {
    private static final String FS = System.getProperty("file.separator");
    private static final Logger logger = LoggerFactory.getLogger(MojoHelper.class);
    Filters filters = new Filters();

    public void execute(MojoContext mc, GraphContext gc, List<GraphContext> descriptors) {
        try {
            if (mc.isSkip()) {
                logger.info("Skipping execution");
                return;
            }
            List<GraphContext> descriptorsToUse = getDescriptorsToUse(mc, gc, descriptors);
            if (Helper.isEmpty(descriptorsToUse)) {
                logger.info("No descriptors");
                return;
            }

            for (GraphContext descriptor : descriptorsToUse) {
                execute(mc, descriptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<GraphContext> getDescriptorsToUse(MojoContext mc, GraphContext gc, List<GraphContext> descriptors) {
        List<GraphContext> descriptorsToUse = new ArrayList<GraphContext>();
        if (descriptors == null) {
            descriptors = new ArrayList<GraphContext>();
        }
        if (mc.isUseDefaultDescriptors()) {
            descriptorsToUse.addAll(getDefaultDescriptors(gc));
        }
        Counter counter = new Counter(1);
        logger.debug("global type={}", gc.getType());
        for (GraphContext descriptor : descriptors) {
            Helper.copyPropertiesIfNull(descriptor, gc);
            if (descriptor.getCategory() == null) {
                descriptor.setCategory("other");
            }
            if (descriptor.getLabel() == null) {
                descriptor.setLabel(counter.increment() + "");
            }
            if (descriptor.getTransitive() == null) {
                descriptor.setTransitive(true);
            }
            if (descriptor.getLayout() == null) {
                descriptor.setLayout(LayoutStyle.CONDENSED);
            }
        }
        Helper.addAll(descriptorsToUse, descriptors);
        for (GraphContext descriptor : descriptorsToUse) {
            String filename = getFilename(mc, descriptor);
            File file = new File(filename);
            descriptor.setFile(file);
            logger.debug(file.getPath());
        }
        return descriptorsToUse;
    }

    protected String getFilename(MojoContext mc, GraphContext gc) {
        String category = gc.getCategory();
        String label = gc.getLabel();
        String type = gc.getType();
        return mc.getOutputDir().getAbsolutePath() + FS + category + FS + label + "." + type;
    }

    protected List<GraphContext> getDefaultDescriptors(GraphContext gc) {
        LayoutStyle layout = LayoutStyle.CONDENSED;
        List<GraphContext> descriptors = new ArrayList<GraphContext>();
        GraphContext allDirect = Helper.copyProperties(GraphContext.class, gc);
        allDirect.setTransitive(false);
        allDirect.setCategory("direct");
        allDirect.setLabel("all");
        allDirect.setLayout(layout);
        descriptors.add(allDirect);
        for (Scope scope : Scope.values()) {
            descriptors.add(getGraphContext(scope, false, layout, gc));
        }
        GraphContext allTransitive = Helper.copyProperties(GraphContext.class, gc);
        allTransitive.setTransitive(true);
        allTransitive.setCategory("transitive");
        allTransitive.setLabel("all");
        allTransitive.setLayout(layout);
        descriptors.add(allTransitive);
        for (Scope scope : Scope.values()) {
            descriptors.add(getGraphContext(scope, true, layout, gc));
        }
        for (Scope scope : Scope.values()) {
            descriptors.add(getGraphContext(scope, true, LayoutStyle.FLAT, gc));
        }
        GraphContext conflicts = Helper.copyProperties(GraphContext.class, gc);
        conflicts.setShow("::conflict");
        conflicts.setPostProcessors(Collections.singletonList(new ConflictsProcessor()));
        conflicts.setCategory("other");
        conflicts.setLabel("conflicts");
        conflicts.setLayout(layout);
        conflicts.setTransitive(true);
        descriptors.add(conflicts);
        return descriptors;
    }

    protected GraphContext getGraphContext(Scope scope, boolean transitive, LayoutStyle layout, GraphContext context) {
        GraphContext gc = Helper.copyProperties(GraphContext.class, context);
        gc.setTransitive(transitive);
        gc.setCategory(transitive ? "transitive" : "direct");
        String label = scope.toString();
        if (LayoutStyle.CONDENSED != layout) {
            label = label + "-" + layout.toString().toLowerCase();
        }
        gc.setLabel(label);
        gc.setLayout(layout);
        return gc;
    }

    public void execute(MojoContext mc, GraphContext gc) {
        if (mc.isSkip()) {
            logger.info("Skipping execution");
            return;
        }

        try {
            logger.info(gc.getFile().getPath());
            EdgeHandler edgeHandler = getEdgeHandler(gc.getLayout());
            gc.setEdgeHandler(edgeHandler);
            GraphHelper gh = new GraphHelper();
            String title = gh.getGraphTitle(gc);
            gc.setTitle(title);
            String content = getDotFileContent(mc, gc);
            gc.setContent(content);
            Dot dot = new Dot();
            dot.fillInContext(gc);
            dot.execute(gc);
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    protected EdgeHandler getEdgeHandler(LayoutStyle layout) {
        switch (layout) {
        case CONDENSED:
            return new CondensedEdgeHandler();
        case FLAT:
            return new FlatEdgeHandler();
        default:
            throw new IllegalArgumentException("Unknown layout " + layout);
        }
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

    public String getDotFileContent(MojoContext mc, GraphContext gc) {
        TreeHelper helper = new TreeHelper();
        DependencyNode mavenTree = getMavenTree(mc);
        Node<MavenContext> tree = helper.getTree(mavenTree);
        for (PreProcessor processor : gc.getPreProcessors()) {
            processor.process(gc, tree);
        }
        helper.validate(tree);
        helper.sanitize(tree);
        if (mc.isVerbose()) {
            TreeMetaData md = helper.getMetaData(tree);
            helper.show(md);
        }
        helper.include(tree, getIncludeFilter(gc));
        helper.exclude(tree, getExcludeFilter(gc));
        List<GraphNode> nodes = helper.getGraphNodes(tree);
        EdgeHandler handler = gc.getEdgeHandler();
        List<Edge> edges = helper.getEdges(tree, handler);
        for (PostProcessor processor : gc.getPostProcessors()) {
            processor.process(gc, tree, edges, nodes);
        }
        if (mc.isVerbose()) {
            helper.show(nodes, edges);
        }
        Graph graph = new GraphHelper().getGraph(gc.getTitle(), gc.getDirection(), nodes, edges);
        return new StringGenerator().getString(graph);
    }

    protected NodeFilter<MavenContext> getShowFilter(GraphContext gc) {
        TokenCollector<MavenContext> collector = new MavenContextTokenCollector();
        Filter<MavenContext> filter = filters.getIncludePatternFilter(gc.getShow(), collector);
        return new MavenContextFilterWrapper(filter);
    }

    protected NodeFilter<MavenContext> getHideFilter(GraphContext gc) {
        TokenCollector<MavenContext> collector = new MavenContextTokenCollector();
        Filter<MavenContext> filter = filters.getExcludePatternFilter(gc.getHide(), collector);
        return new MavenContextFilterWrapper(filter);
    }

    protected NodeFilter<MavenContext> getIncludeFilter(GraphContext gc) {
        TokenCollector<Artifact> collector = new ArtifactIdTokenCollector();
        Filter<Artifact> filter = filters.getIncludePatternFilter(gc.getIncludes(), collector);
        ArtifactFilterWrapper artifactFilter = new ArtifactFilterWrapper(filter);
        List<NodeFilter<MavenContext>> filters = new ArrayList<NodeFilter<MavenContext>>();
        NodeFilter<MavenContext> artifactQualifierFilter = getShowFilter(gc);
        filters.add(artifactQualifierFilter);
        filters.add(artifactFilter);
        return new NodeFilterChain<MavenContext>(filters, MatchCondition.ALL, true);
    }

    protected NodeFilter<MavenContext> getExcludeFilter(GraphContext gc) {
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

    protected DepthFilter<MavenContext> getDepthFilter(GraphContext gc) {
        int maxDepth = gc.getTransitive() ? DepthFilter.INFINITE : 1;
        maxDepth = (gc.getDepth() != null && gc.getDepth() >= 0) ? gc.getDepth() : maxDepth;
        return new DepthFilter<MavenContext>(maxDepth);
    }

}