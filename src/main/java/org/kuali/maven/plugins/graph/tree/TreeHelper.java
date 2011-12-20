package org.kuali.maven.plugins.graph.tree;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.collector.ArtifactIdTokenCollector;
import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.kuali.maven.plugins.graph.collector.VersionFreeArtifactTokenCollector;
import org.kuali.maven.plugins.graph.dot.GraphException;
import org.kuali.maven.plugins.graph.dot.NodeGenerator;
import org.kuali.maven.plugins.graph.dot.edge.EdgeHandler;
import org.kuali.maven.plugins.graph.filter.NodeFilter;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.LabelCount;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.pojo.Style;
import org.kuali.maven.plugins.graph.sanitize.ConflictSanitizer;
import org.kuali.maven.plugins.graph.sanitize.CyclicSanitizer;
import org.kuali.maven.plugins.graph.sanitize.DuplicateSanitizer;
import org.kuali.maven.plugins.graph.sanitize.MavenContextSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public class TreeHelper {
    private static final Logger logger = LoggerFactory.getLogger(TreeHelper.class);
    public static final String ROOT_FILL_COLOR = "#dddddd";
    public static final String OPTIONAL = "optional";
    public static final String REQUIRED = "required";
    Counter counter = new Counter();
    NodeGenerator ng = new NodeGenerator();
    Properties properties = getProperties();

    public void include(Node<MavenContext> node, NodeFilter<MavenContext> filter) {
        if (!filter.isMatch(node) && !node.isRoot()) {
            hide(node);
        } else {
            showPath(node);
        }
        for (Node<MavenContext> child : node.getChildren()) {
            include(child, filter);
        }
    }

    public void showPath(Node<MavenContext> node) {
        Node<MavenContext>[] path = node.getPath();
        for (Node<MavenContext> pathNode : path) {
            show(pathNode);
        }
    }

    public void exclude(Node<MavenContext> node, NodeFilter<MavenContext> filter) {
        if (filter.isMatch(node) && !node.isRoot()) {
            logger.debug("hiding tree at level=" + node.getLevel());
            hideTree(node);
        }
        for (Node<MavenContext> child : node.getChildren()) {
            exclude(child, filter);
        }
    }

    public void hideTree(Node<MavenContext> node) {
        hide(node);
        for (Node<MavenContext> child : node.getChildren()) {
            hideTree(child);
        }
    }

    public void show(Node<MavenContext> node) {
        node.getObject().getGraphNode().setHidden(false);
    }

    public void hide(Node<MavenContext> node) {
        node.getObject().getGraphNode().setHidden(true);
    }

    public <T> void prune(Node<T> node, NodeFilter<T> filter) {
        if (!filter.isMatch(node)) {
            if (node.isRoot()) {
                logger.debug("removing all children from root");
                node.removeAllChildren();
            } else {
                logger.debug("removing node at level={}", node.getLevel());
                node.removeFromParent();
            }
        } else {
            for (Node<T> child : node.getChildren()) {
                prune(child, filter);
            }
        }
    }

    protected MavenContext getMavenContext(GraphNode gn, DependencyNode dn) {
        int id = gn.getId();
        String artifactIdentifier = getArtifactId(dn.getArtifact());
        MavenContext context = new MavenContext();
        context.setId(id);
        context.setArtifactIdentifier(artifactIdentifier);
        context.setGraphNode(gn);
        context.setDependencyNode(dn);
        context.setState(State.getState(dn.getState()));
        return context;
    }

    public Node<MavenContext> getTree(DependencyNode dependencyNode) {
        GraphNode gn = getGraphNode(dependencyNode);
        MavenContext context = getMavenContext(gn, dependencyNode);
        Node<MavenContext> node = new Node<MavenContext>(context);
        @SuppressWarnings("unchecked")
        List<DependencyNode> children = dependencyNode.getChildren();
        for (DependencyNode child : children) {
            node.add(getTree(child));
        }
        return node;
    }

    public void sanitize(Node<MavenContext> node) {
        List<Node<MavenContext>> nodes = node.getBreadthFirstList();
        logger.info("Sanitizing metadata for " + nodes.size() + " dependency nodes");
        Map<String, MavenContext> included = getMap(node, nodes, State.INCLUDED);
        int includedCount = getStateCount(nodes, State.INCLUDED);

        Assert.isTrue(includedCount == included.size(), "Unique included artifact id counts don't match. size="
                + included.size() + " count=" + includedCount);

        List<MavenContextSanitizer> sanitizers = getSanitizers(included);
        for (MavenContextSanitizer sanitizer : sanitizers) {
            sanitizer.sanitize(node);
        }
        for (Node<MavenContext> element : nodes) {
            updateGraphNodeStyle(element.getObject());
        }
    }

    protected List<MavenContextSanitizer> getSanitizers(Map<String, MavenContext> included) {
        List<MavenContextSanitizer> sanitizers = new ArrayList<MavenContextSanitizer>();
        sanitizers.add(new DuplicateSanitizer(included));
        sanitizers.add(new ConflictSanitizer(included));
        sanitizers.add(new CyclicSanitizer(included));
        return sanitizers;
    }

    protected boolean replacementFound(MavenContext context, Map<String, MavenContext> included) {
        if (included.get(context.getArtifactIdentifier()) != null) {
            return true;
        }
        Artifact related = context.getDependencyNode().getRelatedArtifact();
        if (related == null) {
            return false;
        } else {
            String artifactId = getArtifactId(related);
            return (included.get(artifactId) != null);
        }
    }

    protected int getStateCount(List<Node<MavenContext>> nodes, State state) {
        int count = 0;
        for (Node<MavenContext> node : nodes) {
            MavenContext context = node.getObject();
            DependencyNode dn = context.getDependencyNode();
            State nodeState = State.getState(dn.getState());
            count = (state == nodeState) ? ++count : count;
        }
        return count;
    }

    public List<MavenContext> getList(Node<MavenContext> node, State state) {
        Assert.notNull(state, "state is required");
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        List<MavenContext> newList = new ArrayList<MavenContext>();
        for (Node<MavenContext> element : list) {
            MavenContext context = element.getObject();
            DependencyNode dn = context.getDependencyNode();
            State elementState = State.getState(dn.getState());
            if (!state.equals(elementState)) {
                continue;
            }
            newList.add(context);
        }
        return newList;
    }

    public Map<String, MavenContext> getMap(Node<MavenContext> node, List<Node<MavenContext>> list, State state) {
        Map<String, MavenContext> map = new HashMap<String, MavenContext>();
        for (Node<MavenContext> element : list) {
            MavenContext context = element.getObject();
            DependencyNode dn = context.getDependencyNode();
            State elementState = State.getState(dn.getState());
            if (!state.equals(elementState)) {
                continue;
            } else {
                map.put(context.getArtifactIdentifier(), context);
            }
        }
        return map;
    }

    public void show(TreeMetaData md) {
        logger.info("Metadata for " + md.getSize() + " dependency nodes");
        logger.info("states -" + toString(md.getStates()));
        logger.info("requiredness -" + toString(md.getRequiredness()));
        logger.info("scopes -" + toString(md.getScopes()));
        logger.info("types -" + toString(md.getTypes()));
        logger.info("classifiers -" + toString(md.getClassifiers()));
        int groups = md.getGroupIds().size();
        int artifacts = md.getArtifactIds().size();
        int versions = md.getVersions().size();
        logger.info("unique gav info - groups:" + groups + " artifacts:" + artifacts + " versions:" + versions);
        logger.info("unique artifacts (including version): " + md.getArtifactIdentifiers().size());
        logger.info("unique artifacts  (ignoring version): " + md.getPartialArtifactIdentifiers().size());
    }

    protected List<String> getArtifactIds(Collection<String> ids) {
        List<String> strings = new ArrayList<String>(ids);
        Collections.sort(strings);
        return strings;
    }

    protected String toString(Tracker tracker) {
        List<LabelCount> labels = new ArrayList<LabelCount>();
        for (String key : tracker.keySet()) {
            labels.add(new LabelCount(key, tracker.get(key)));
        }
        Collections.sort(labels);
        Collections.reverse(labels);
        StringBuilder sb = new StringBuilder();
        for (LabelCount label : labels) {
            sb.append(" " + label.getLabel() + ":" + label.getCount());
        }
        return sb.toString();
    }

    protected <T> void show(String label, List<T> list) {
        logger.info(label);
        for (T element : list) {
            logger.info(element.toString());
        }
    }

    public TreeMetaData getMetaData(Node<MavenContext> node) {
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        TreeMetaData metaData = new TreeMetaData();
        metaData.setSize(list.size());
        for (Node<MavenContext> element : list) {
            updateMetaData(metaData, element.getObject());
        }
        return metaData;
    }

    protected void updateMetaData(TreeMetaData md, MavenContext context) {
        DependencyNode dn = context.getDependencyNode();
        updateMetaData(md, dn.getArtifact());
        if (dn.getParent() != null) {
            md.getStates().increment(context.getState().getValue());
        }
    }

    protected void updateMetaData(TreeMetaData md, Artifact a) {
        md.getGroupIds().increment(a.getGroupId());
        md.getArtifactIds().increment(a.getArtifactId());
        md.getTypes().increment(a.getType());
        String classifier = a.getClassifier();
        if (!Helper.isBlank(classifier)) {
            md.getClassifiers().increment(classifier);
        }
        md.getVersions().increment(a.getVersion());
        Scope scope = Scope.getScope(a.getScope());
        if (scope != null) {
            md.getScopes().increment(scope.toString());
        }
        md.getRequiredness().increment(a.isOptional() ? OPTIONAL : REQUIRED);
        md.getArtifactIdentifiers().add(getArtifactId(a));
        md.getPartialArtifactIdentifiers().add(getPartialArtifactId(a));
    }

    protected void updateGraphNodeStyle(MavenContext context) {
        DependencyNode dn = context.getDependencyNode();
        boolean optional = dn.getArtifact().isOptional();
        State state = context.getState();
        Scope scope = Scope.getScope(dn.getArtifact().getScope());
        Style style = getStyle(scope, optional, state);
        copyStyleProperties(context.getGraphNode(), style);
    }

    protected GraphNode getGraphNode(DependencyNode dn) {
        Artifact a = dn.getArtifact();
        GraphNode n = new GraphNode();
        n.setId(counter.increment());
        n.setLabel(ng.getLabel(a));
        String fillcolor = dn.getParent() == null ? ROOT_FILL_COLOR : n.getFillcolor();
        n.setFillcolor(fillcolor);
        return n;
    }

    public List<GraphNode> getGraphNodes(Node<MavenContext> node) {
        List<GraphNode> nodes = new ArrayList<GraphNode>();
        if (node.isRoot()) {
            nodes.add(node.getObject().getGraphNode());
        }
        List<Node<MavenContext>> children = node.getChildren();
        for (Node<MavenContext> child : children) {
            nodes.add(child.getObject().getGraphNode());
            nodes.addAll(getGraphNodes(child));
        }
        return nodes;
    }

    public List<Edge> getEdges(Node<MavenContext> node, EdgeHandler handler) {
        List<Edge> edges = new ArrayList<Edge>();
        List<Node<MavenContext>> children = node.getChildren();
        for (Node<MavenContext> child : children) {
            edges.addAll(handler.getEdges(child));
            edges.addAll(getEdges(child, handler));
        }
        return edges;
    }

    // Omit normal info from the label
    public static String getRelationshipLabel(Scope scope, boolean optional, State state) {
        List<String> labelTokens = new ArrayList<String>();
        if (!Scope.DEFAULT_SCOPE.equals(scope)) {
            labelTokens.add(scope.name().toLowerCase());
        }
        if (optional) {
            labelTokens.add(OPTIONAL);
        }
        if (!State.INCLUDED.equals(state)) {
            labelTokens.add(state.getValue());
        }
        return toIdString(labelTokens);
    }

    public static String toIdString(List<String> strings) {
        return toIdString(Helper.toArray(strings));
    }

    public static String toIdString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                sb.append(":");
            }
            sb.append(Helper.toEmpty(strings[i]));
        }
        return sb.toString();
    }

    public void copyStyleProperties(Object dest, Style style) {
        List<String> names = getStyleProperties();
        for (String name : names) {
            String value = getProperty(style, name);
            if (!Helper.isBlank(value)) {
                copyProperty(dest, name, value);
            }
        }
    }

    protected List<String> getStyleProperties() {
        try {
            @SuppressWarnings("unchecked")
            Map<String, ?> map = BeanUtils.describe(Style.DEFAULT_STYLE);
            map.remove("class");
            return new ArrayList<String>(map.keySet());
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    protected String getStyle(String property, Scope scope, boolean optional, State state) {
        // State styling overrides everything
        String key1 = "state." + state.getValue() + "." + property;
        // Scope styling overrides "optional" properties
        String key2 = "scope." + scope.getValue() + "." + property;
        // Fall through to styling for the "optional" attribute on a dependency
        String key3 = "optional." + property;

        String value1 = properties.getProperty(key1);
        String value2 = properties.getProperty(key2);
        String value3 = properties.getProperty(key3);

        if (!Helper.isBlank(value1)) {
            return value1;
        } else if (!Helper.isBlank(value2)) {
            return value2;
        } else if (!Helper.isBlank(value3) && optional) {
            return value3;
        } else {
            return null;
        }

    }

    public Style getStyle(Scope scope, boolean optional, State state) {
        // This happens for the root node
        scope = (scope == null) ? Scope.DEFAULT_SCOPE : scope;
        state = (state == null) ? State.INCLUDED : state;

        List<String> properties = getStyleProperties();
        Style style = new Style();

        for (String property : properties) {
            String value = getStyle(property, scope, optional, state);
            if (Helper.isBlank(value)) {
                continue;
            }
            copyProperty(style, property, value);
        }
        return style;
    }

    protected Properties getProperties() {
        String location = "classpath:dot.properties";
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(location);
        InputStream in = null;
        try {
            Properties properties = new Properties();
            in = resource.getInputStream();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new GraphException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    protected String getProperty(Object bean, String name) {
        try {
            return BeanUtils.getProperty(bean, name);
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    protected void copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.copyProperty(bean, name, value);
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

    public void show(List<GraphNode> nodes, List<Edge> edges) {
        int nodeCount = nodes.size();
        int edgeCount = edges.size();
        int nodeCountShown = getNodeCount(nodes);
        int edgeCountShown = getEdgeCount(edges);
        int nodeCountHidden = nodeCount - nodeCountShown;
        int edgeCountHidden = edgeCount - edgeCountShown;
        logger.info("Generated " + nodes.size() + " graph nodes and " + edges.size() + " edges");
        logger.info("Showing " + nodeCountShown + " nodes and " + edgeCountShown + " edges");
        logger.info("Hiding " + nodeCountHidden + " nodes and " + edgeCountHidden + " edges");
    }

    protected int getEdgeCount(List<Edge> edges) {
        int count = 0;
        for (Edge edge : edges) {
            GraphNode parent = edge.getParent();
            GraphNode child = edge.getChild();
            boolean hidden = parent.isHidden() || child.isHidden();
            count = hidden ? count : ++count;
        }
        return count;
    }

    protected int getNodeCount(List<GraphNode> nodes) {
        int count = 0;
        for (GraphNode node : nodes) {
            count = node.isHidden() ? count : ++count;
        }
        return count;
    }

    /**
     * [groupId]:[artifactId]:[type]:[classifier]
     */
    public static String getPartialArtifactId(Artifact a) {
        TokenCollector<Artifact> collector = new VersionFreeArtifactTokenCollector();
        return toIdString(collector.getTokens(a));
    }

    /**
     * [groupId]:[artifactId]:[type]:[classifier]:[version]
     */
    public static String getArtifactId(Artifact a) {
        TokenCollector<Artifact> collector = new ArtifactIdTokenCollector();
        return toIdString(collector.getTokens(a));
    }

}
