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
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.Layout;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.processor.CascadeOptionalProcessor;
import org.kuali.maven.plugins.graph.processor.FlatEdgeProcessor;
import org.kuali.maven.plugins.graph.processor.HideConflictsProcessor;
import org.kuali.maven.plugins.graph.processor.HideDuplicatesProcessor;
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
    private static final String FS = System.getProperty("file.separator");
    private static final Logger logger = LoggerFactory.getLogger(MojoHelper.class);
    Filters filters = new Filters();

    public Filter<Node<MavenContext>> getIncludeExcludeFilter(GraphDescriptor descriptor) {
        NodeFilter<MavenContext> include = getIncludeFilter(descriptor);
        NodeFilter<MavenContext> exclude = getExcludeFilter(descriptor);
        return new IncludeExcludeFilter<Node<MavenContext>>(include, exclude);
    }

    public void execute(MojoContext mc, GraphDescriptor gc, List<GraphDescriptor> descriptors) {
        try {
            if (mc.isSkip()) {
                logger.info("Skipping execution");
                return;
            }
            List<GraphDescriptor> descriptorsToUse = getDescriptorsToUse(mc, gc, descriptors);
            if (Helper.isEmpty(descriptorsToUse)) {
                logger.info("No descriptors");
                return;
            }

            for (GraphDescriptor descriptor : descriptorsToUse) {
                execute(mc, descriptor);
            }
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    protected List<GraphDescriptor> getDescriptorsToUse(MojoContext mc, GraphDescriptor gc,
            List<GraphDescriptor> descriptors) {
        List<GraphDescriptor> descriptorsToUse = new ArrayList<GraphDescriptor>();
        if (descriptors == null) {
            descriptors = new ArrayList<GraphDescriptor>();
        }
        if (mc.isUseDefaultDescriptors()) {
            descriptorsToUse.addAll(getDefaultDescriptors(gc));
        }
        logger.info("descriptor count={}", descriptorsToUse.size());
        Counter counter = new Counter(1);
        logger.debug("global type={}", gc.getOutputFormat());
        for (GraphDescriptor descriptor : descriptors) {
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
                descriptor.setLayout(Layout.LINKED);
            }
        }
        Helper.addAll(descriptorsToUse, descriptors);
        for (GraphDescriptor descriptor : descriptorsToUse) {
            String filename = getFilename(mc, descriptor);
            File file = new File(filename);
            descriptor.setFile(file);
            logger.debug(file.getPath());
        }
        return descriptorsToUse;
    }

    protected String getFilename(MojoContext mc, GraphDescriptor gc) {
        String category = gc.getCategory();
        String label = gc.getLabel();
        String type = gc.getOutputFormat();
        return mc.getOutputDir().getAbsolutePath() + FS + category + FS + label + "." + type;
    }

    protected List<GraphDescriptor> getDefaultDescriptors(GraphDescriptor gc) {
        List<GraphDescriptor> flat = new ArrayList<GraphDescriptor>();
        flat.addAll(getGraphDescriptors(null, gc));
        for (Scope scope : Scope.values()) {
            flat.addAll(getGraphDescriptors(scope, gc));
        }
        List<GraphDescriptor> linked = new ArrayList<GraphDescriptor>();
        for (GraphDescriptor descriptor : flat) {
            linked.add(Helper.copyProperties(GraphDescriptor.class, descriptor));
            descriptor.setLabel(descriptor.getLabel() + "-flat");
        }
        for (GraphDescriptor descriptor : linked) {
            descriptor.setLayout(Layout.LINKED);
        }
        List<GraphDescriptor> both = new ArrayList<GraphDescriptor>(flat);
        both.addAll(linked);
        return both;
    }

    protected String getFilter(Scope scope, Boolean optional) {
        StringBuilder sb = new StringBuilder();
        sb.append(scope == null ? "*" : scope.toString());
        sb.append(":");
        sb.append(optional == null ? "*" : (optional ? "optional" : "required"));
        return sb.toString();
    }

    protected String getLabel(Boolean optional) {
        if (optional == null) {
            return "";
        } else {
            return optional ? "optional" : "required";
        }
    }

    protected List<GraphDescriptor> getGraphDescriptors(Scope scope, GraphDescriptor context) {
        List<GraphDescriptor> contexts = new ArrayList<GraphDescriptor>();

        // optional or required
        List<GraphDescriptor> list1 = getGraphDescriptors(scope, null, context);

        // optional only
        // List<GraphDescriptor> list2 = getGraphDescriptors(scope, true, context);

        // required only
        // List<GraphDescriptor> list3 = getGraphDescriptors(scope, false, context);

        // Add them to the list
        contexts.addAll(list1);
        // contexts.addAll(list2);
        // contexts.addAll(list3);
        return contexts;
    }

    protected List<GraphDescriptor> getGraphDescriptors(Scope scope, Boolean optional, GraphDescriptor context) {
        List<GraphDescriptor> contexts = new ArrayList<GraphDescriptor>();
        String show = getFilter(scope, optional);

        // transitive
        contexts.add(getGraphDescriptor(context, scope, show, true));

        // non-transitive
        contexts.add(getGraphDescriptor(context, scope, show, false));
        return contexts;
    }

    protected GraphDescriptor getGraphDescriptor(GraphDescriptor context, Scope scope, String show, boolean transitive) {
        String label = scope == null ? "dependencies" : scope.toString();
        GraphDescriptor gc = Helper.copyProperties(GraphDescriptor.class, context);
        gc.setShow(show);
        gc.setLabel(label);
        gc.setTransitive(transitive);
        gc.setLayout(Layout.FLAT);
        String category = transitive ? "transitive" : "direct";
        gc.setCategory(category);
        return gc;
    }

    protected GraphDescriptor getGraphDescriptor(Scope scope, Boolean transitive, Layout layout, GraphDescriptor context) {
        GraphDescriptor gc = Helper.copyProperties(GraphDescriptor.class, context);
        gc.setTransitive(transitive);
        gc.setCategory(transitive ? "transitive" : "direct");
        String label = scope == null ? "all" : scope.toString();
        String show = scope == null ? null : scope.toString();
        if (Layout.LINKED != layout) {
            label = label + "-" + layout.toString().toLowerCase();
        }
        gc.setShow(show);
        gc.setLabel(label);
        gc.setLayout(layout);
        return gc;
    }

    public void execute(MojoContext mc, GraphDescriptor gc) {
        if (mc.isSkip()) {
            logger.info("Skipping execution");
            return;
        }

        try {
            Node<MavenContext> tree = getProcessedTree(mc, gc);
            Graph graph = getGraph(tree, mc, gc);
            if (isEmptyGraph(graph) && Boolean.TRUE.equals(gc.getSkipEmptyGraphs())) {
                logger.info("Skipping empty graph");
                return;
            }
            String content = getDotFileContent(graph);
            Dot dot = new Dot();
            dot.fillInContext(gc, content);
            logger.info(gc.getFile().getPath());
            dot.execute(gc);
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
        // processors.add(new FilteringProcessor(gc));
        processors.add(new ShowPathsProcessor(gc));
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
