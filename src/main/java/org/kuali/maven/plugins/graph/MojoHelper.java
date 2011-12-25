package org.kuali.maven.plugins.graph;

import java.util.ArrayList;
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
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.PostProcessor;
import org.kuali.maven.plugins.graph.tree.PreProcessor;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.tree.TreeMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MojoHelper {
    private static final Logger logger = LoggerFactory.getLogger(MojoHelper.class);
    Filters filters = new Filters();

    public void execute(MojoContext mc, GraphContext gc) {
        if (mc.isSkip()) {
            logger.info("Skipping execution");
            return;
        }

        try {
            EdgeHandler edgeHandler = getEdgeHandler(gc.getLayout());
            gc.setEdgeHandler(edgeHandler);
            GraphHelper gh = new GraphHelper();
            String title = gh.getGraphTitle(gc);
            gc.setTitle(title);
            logger.info("title=" + title);
            String content = getDotFileContent(mc, gc);
            gc.setContent(content);
            Dot dot = new Dot();
            dot.fillInContext(gc);
            dot.execute(gc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected EdgeHandler getEdgeHandler(LayoutStyle style) {
        switch (style) {
        case CONDENSED:
            return new CondensedEdgeHandler();
        case FLAT:
            return new FlatEdgeHandler();
        default:
            throw new IllegalArgumentException("Unknown style " + style);
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
        int maxDepth = gc.isTransitive() ? DepthFilter.INFINITE : 1;
        maxDepth = gc.getDepth() >= 0 ? gc.getDepth() : maxDepth;
        return new DepthFilter<MavenContext>(maxDepth);
    }

}
