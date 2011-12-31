package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
import java.util.ArrayList;
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
import org.kuali.maven.plugins.graph.pojo.Group;
import org.kuali.maven.plugins.graph.pojo.Layout;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.processor.CascadeOptionalProcessor;
import org.kuali.maven.plugins.graph.processor.FlatEdgeProcessor;
import org.kuali.maven.plugins.graph.processor.HideConflictsProcessor;
import org.kuali.maven.plugins.graph.processor.HideDuplicatesProcessor;
import org.kuali.maven.plugins.graph.processor.HidingProcessor;
import org.kuali.maven.plugins.graph.processor.LabelProcessor;
import org.kuali.maven.plugins.graph.processor.LinkedEdgeProcessor;
import org.kuali.maven.plugins.graph.processor.Processor;
import org.kuali.maven.plugins.graph.processor.SanitizingProcessor;
import org.kuali.maven.plugins.graph.processor.ShowMetadataProcessor;
import org.kuali.maven.plugins.graph.processor.ShowPathsProcessor;
import org.kuali.maven.plugins.graph.processor.StyleProcessor;
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
        if (Helper.isEmpty(categories)) {
            logger.info("No categories");
            return;
        }
        for (Category category : categories) {
            for (Group group : category.getGroups()) {
                fillInDescriptors(gc, group.getDescriptors(), mc.getOutputDir());
                List<GraphDescriptor> executed = execute(mc, gc, group.getDescriptors());
                group.setDescriptors(executed);
            }
        }
    }

    public List<GraphDescriptor> execute(MojoContext mc, GraphDescriptor gc, List<GraphDescriptor> descriptors) {
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

    protected void fillInDescriptors(GraphDescriptor gd, List<GraphDescriptor> gds, File outputDir) {
        logger.debug("default output format={}", gd.getOutputFormat());
        Counter counter = new Counter(1);
        for (GraphDescriptor descriptor : gds) {
            Helper.copyPropertiesIfNull(descriptor, gd);
            if (descriptor.getName() == null) {
                descriptor.setName(counter.increment() + "");
            }
            if (descriptor.getTransitive() == null) {
                descriptor.setTransitive(true);
            }
            if (descriptor.getLayout() == null) {
                descriptor.setLayout(Layout.LINKED);
            }
            File file = new File(outputDir, getRelativePath(descriptor));
            descriptor.setFile(file);
        }
    }

    protected List<Category> getDefaultCategories(GraphDescriptor gd) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(getCategory(gd, false));
        categories.add(getCategory(gd, true));
        return categories;
    }

    protected String getRelativePath(GraphDescriptor gd) {
        StringBuilder sb = new StringBuilder();
        sb.append(gd.getGroup().getCategory().getName());
        sb.append("/");
        sb.append(gd.getGroup().getName());
        sb.append("/");
        sb.append(gd.getName());
        sb.append(".");
        sb.append(gd.getOutputFormat());
        return sb.toString();
    }

    protected Category getCategory(GraphDescriptor gd, boolean transitive) {
        String name = getTransitiveLabel(transitive);
        Category c = new Category(name);
        c.setGroups(getGroups(gd, transitive));
        for (Group group : c.getGroups()) {
            group.setCategory(c);
        }
        return c;
    }

    protected String getTransitiveLabel(boolean transitive) {
        return transitive ? "transitive" : "direct";
    }

    protected String getScopeLabel(Scope scope) {
        return scope == null ? "all" : scope.toString();
    }

    protected List<Group> getGroups(GraphDescriptor gd, boolean transitive) {
        List<Group> groups = new ArrayList<Group>();
        Group any = new Group(getScopeLabel(null));
        any.setDescriptors(getDescriptors(gd, any, transitive, null));
        groups.add(any);
        for (Scope scope : Scope.values()) {
            Group group = new Group(getScopeLabel(scope));
            group.setDescriptors(getDescriptors(gd, group, transitive, scope));
            groups.add(group);
        }
        return groups;
    }

    protected List<GraphDescriptor> getDescriptors(GraphDescriptor gd, Group group, boolean transitive, Scope scope) {
        List<GraphDescriptor> descriptors = new ArrayList<GraphDescriptor>();
        for (Layout layout : Layout.values()) {
            descriptors.add(getDescriptor(gd, group, transitive, scope, layout));
        }
        return descriptors;
    }

    protected GraphDescriptor getDescriptor(GraphDescriptor gd, Group group, boolean transitive, Scope scope,
            Layout layout) {
        GraphDescriptor descriptor = Helper.copyProperties(GraphDescriptor.class, gd);
        descriptor.setShow(scope == null ? "*" : scope.toString());
        descriptor.setTransitive(transitive);
        descriptor.setName(layout.toString().toLowerCase());
        descriptor.setLayout(layout);
        descriptor.setKeepDotFile(true);
        descriptor.setGroup(group);
        return descriptor;
    }

    protected String getLabel(Scope scope, Layout layout) {
        StringBuilder sb = new StringBuilder();
        sb.append(scope == null ? "dependencies" : scope.toString());
        sb.append(layout == Layout.LINKED ? "" : "-flat");
        return sb.toString();
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
            logger.info(gc.getFile().getPath());
            dot.execute(gc);
            return gc;
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
        processors.add(new ValidatingProcessor());
        processors.add(new SanitizingProcessor());
        processors.add(new LabelProcessor(gc));
        if (Boolean.TRUE.equals(gc.getCascadeOptional())) {
            processors.add(new CascadeOptionalProcessor());
        }
        if (verbose) {
            processors.add(new ShowMetadataProcessor());
        }
        processors.add(getHideShowProcessor(gc));
        processors.add(new StyleProcessor());
        processors.add(getEdgeProcessor(gc.getLayout()));
        if (!Boolean.TRUE.equals(gc.getShowDuplicates())) {
            processors.add(new HideDuplicatesProcessor());
        }
        if (!Boolean.TRUE.equals(gc.getShowConflicts())) {
            processors.add(new HideConflictsProcessor(gc.getLayout()));
        }
        return processors;
    }

    protected Processor getHideShowProcessor(GraphDescriptor gd) {
        switch (gd.getFilterType()) {
        case HIDE:
            return new HidingProcessor(gd);
        case PATH:
            return new ShowPathsProcessor(gd, false);
        default:
            throw new IllegalStateException("Unknown filter type " + gd.getFilterType());
        }

    }

    protected Node<MavenContext> getProcessedTree(MojoContext mc, GraphDescriptor gc) {
        TreeHelper helper = new TreeHelper();
        if (mc.getMavenTree() == null) {
            DependencyNode mavenTree = getMavenTree(mc);
            mc.setMavenTree(mavenTree);
        }
        Node<MavenContext> tree = helper.getTree(mc.getMavenTree());
        List<Processor> processors = getProcessors(gc, mc.isVerbose());
        for (Processor processor : processors) {
            processor.process(tree);
        }
        return tree;
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

    protected Processor getEdgeProcessor(Layout layout) {
        switch (layout) {
        case LINKED:
            return new LinkedEdgeProcessor();
        case FLAT:
            return new FlatEdgeProcessor();
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
