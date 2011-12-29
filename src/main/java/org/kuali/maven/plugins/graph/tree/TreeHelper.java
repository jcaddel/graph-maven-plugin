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
package org.kuali.maven.plugins.graph.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.codehaus.plexus.util.StringUtils;
import org.kuali.maven.plugins.graph.collector.ArtifactIdTokenCollector;
import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.kuali.maven.plugins.graph.collector.VersionFreeArtifactTokenCollector;
import org.kuali.maven.plugins.graph.dot.EdgeHandler;
import org.kuali.maven.plugins.graph.dot.GraphHelper;
import org.kuali.maven.plugins.graph.filter.Filter;
import org.kuali.maven.plugins.graph.filter.NodeFilter;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 * Various helper methods for working with trees.
 * </p>
 *
 * @author jeffcaddel
 */
public class TreeHelper {
    private static final Logger logger = LoggerFactory.getLogger(TreeHelper.class);
    public static final String ROOT_FILL_COLOR = "#dddddd";
    public static final String OPTIONAL = "optional";
    public static final String REQUIRED = "required";
    Counter counter = new Counter();
    GraphHelper graphHelper = new GraphHelper();

    public void filterButShowPath(Node<MavenContext> node, Filter<Node<MavenContext>> filter) {
        List<Node<MavenContext>> hidden = new ArrayList<Node<MavenContext>>();
        List<Node<MavenContext>> displayed = new ArrayList<Node<MavenContext>>();

        List<Node<MavenContext>> list = node.getBreadthFirstList();
        for (Node<MavenContext> element : list) {
            boolean display = filter.isMatch(element) || element.isRoot();
            if (display) {
                displayed.add(element);
            } else {
                hidden.add(element);
            }
        }
        logger.info("hide list size={}", hidden.size());
        logger.info("display list size={}", hidden.size());

        for (Node<MavenContext> element : hidden) {
            hideTree(element);
        }
        for (Node<MavenContext> element : displayed) {
            showPath(element);
        }
    }

    /**
     * <p>
     * Logic for including nodes in a tree only if they match a filter.
     * </p>
     *
     * <p>
     * Any node where <code>filter.isMatch()</code> returns false, is hidden from view.
     * </p>
     *
     * <p>
     * The root node is never hidden, even if <code>filter.isMatch()</code> returns false on the root node.
     * </p>
     *
     * <p>
     * Any node where <code>filter.isMatch()</code> returns true, is displayed. Additionally, any nodes in the path from
     * that node back to the root node are displayed.
     * </p>
     *
     * @param node
     * @param filter
     */
    public void include(Node<MavenContext> node, NodeFilter<MavenContext> filter) {
        if (!filter.isMatch(node) && !node.isRoot()) {
            hide(node);
            logger.debug("i:hiding {}", node.getObject().getArtifactIdentifier());
        } else {
            logger.debug("i:showing path {}", node.getObject().getArtifactIdentifier());
            showPath(node);
        }
        for (Node<MavenContext> child : node.getChildren()) {
            include(child, filter);
        }
    }

    /**
     * <p>
     * Logic for excluding nodes from a tree if they match a filter.
     * </p>
     *
     * <p>
     * Any node where <code>filter.isMatch()</code> returns true, is hidden from view. The entire sub-tree rooted at
     * that node is also hidden from view.
     * </p>
     *
     * <p>
     * The root node is never hidden, even if <code>filter.isMatch()</code> returns true on the root node.
     * </p>
     *
     * <p>
     * Any node where <code>filter.isMatch()</code> returns false, is left untouched.
     * </p>
     *
     * @param node
     * @param filter
     */
    public void exclude(Node<MavenContext> node, NodeFilter<MavenContext> filter) {
        if (filter.isMatch(node) && !node.isRoot()) {
            logger.debug("e:hiding {}", node.getObject().getArtifactIdentifier());
            hideTree(node);
        }
        for (Node<MavenContext> child : node.getChildren()) {
            exclude(child, filter);
        }
    }

    /**
     * <p>
     * Display every node in path from this node back to the root.
     * </p>
     */
    public void showPath(Node<MavenContext> node) {
        Node<MavenContext>[] path = node.getPath();
        List<Node<MavenContext>> pathList = Arrays.asList(path);
        Collections.reverse(pathList);
        for (Node<MavenContext> pathNode : pathList) {
            show(pathNode);
        }
    }

    /**
     * <p>
     * Hide this node, and every node in the sub-tree below this node.
     * </p>
     *
     * @param node
     */
    public void hideTree(Node<MavenContext> node) {
        hide(node);
        for (Node<MavenContext> child : node.getChildren()) {
            hideTree(child);
        }
    }

    /**
     * <p>
     * Convenience method setting the hidden flag to false on the graph node contained inside the
     * <code>MavenContext</code>.
     * <p>
     *
     * @param node
     */
    public void show(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        GraphNode gn = context.getGraphNode();
        if (gn.isHidden()) {
            logger.debug("showing node {}: {}", lpad(context.getId(), 4), context.getArtifactIdentifier());
            gn.setHidden(false);
        }
    }

    /**
     * <p>
     * Convenience method setting the hidden flag to true on the graph node contained inside the
     * <code>MavenContext</code>.
     * <p>
     *
     * @param node
     */
    public void hide(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        GraphNode gn = node.getObject().getGraphNode();
        if (!gn.isHidden()) {
            logger.debug(" hiding node {}: {}", lpad(context.getId(), 4), context.getArtifactIdentifier());
            gn.setHidden(true);
        }
    }

    /**
     * <p>
     * Logic for altering a tree via removal of nodes that do not match the filter criteria.
     * <p>
     *
     * <p>
     * If the root node does not match the filter criteria, all of it's children are removed. Otherwise, the node and
     * all of its children are both removed.
     * </p>
     *
     * @param node
     */
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

    /**
     * <p>
     * Convenience method for generating a new MavenContext object from a <code>GrapNode</code> and a
     * <code>DependencyNode</code>.
     * </p>
     *
     * @param gn
     * @param dn
     * @return
     */
    protected MavenContext getMavenContext(GraphNode gn, DependencyNode dn) {
        int id = gn.getId();
        String artifactIdentifier = getArtifactId(dn.getArtifact());
        MavenContext context = new MavenContext();
        context.setId(id);
        context.setArtifactIdentifier(artifactIdentifier);
        context.setArtifact(dn.getArtifact());
        context.setGraphNode(gn);
        context.setDependencyNode(dn);
        context.setState(State.getState(dn.getState()));
        context.setOptional(dn.getArtifact().isOptional());
        return context;
    }

    /**
     * <p>
     * Given a Maven <code>DependencyNode</code> tree return a type safe <code>Node</code> tree.
     * </p>
     *
     * @param dependencyNode
     * @return
     */
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

    /**
     * <p>
     * Convenience method for obtaining a <code>State</code> object from the <code>int</code> supplied by Maven in the
     * embedded <code>DependencyNode</code>
     * </p>
     *
     * @param node
     * @return
     */
    public State getState(Node<MavenContext> node) {
        return State.getState(node.getObject().getDependencyNode().getState());
    }

    /**
     * <p>
     * Return true, if and only if, the two artifacts are an exact match of each other.
     * </p>
     *
     * <p>
     * More precisely, return true if [groupId]:[artfifactId]:[type]:[classifier]:[version] are an exact match, false
     * otherwise.
     * </p>
     *
     * @param a1
     * @param a2
     * @return
     */
    public boolean equals(Artifact a1, Artifact a2) {
        String id1 = getArtifactId(a1);
        String id2 = getArtifactId(a2);
        return id1.equals(id2);
    }

    /**
     * <p>
     * Return true, if and only if, the two artifacts are the same except for version.
     * </p>
     *
     * <p>
     * More precisely, return true if [groupId]:[artfifactId]:[type]:[classifier] are an exact match, false otherwise
     * </p>
     *
     * @param a1
     * @param a2
     * @return
     */
    public boolean similar(Artifact a1, Artifact a2) {
        String n1 = getPartialArtifactId(a1);
        String n2 = getPartialArtifactId(a2);
        return n1.equals(n2);
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

    protected boolean isMatch(State state, State[] states) {
        for (State arrayState : states) {
            if (state == arrayState) {
                return true;
            }
        }
        return false;
    }

    public List<Node<MavenContext>> getNodeList(Node<MavenContext> node, State... states) {
        Assert.notNull(states, "states are required");
        List<Node<MavenContext>> contexts = new ArrayList<Node<MavenContext>>();
        for (Node<MavenContext> element : node.getBreadthFirstList()) {
            MavenContext context = element.getObject();
            State elementState = context.getState();
            if (isMatch(elementState, states)) {
                contexts.add(element);
            }
        }
        return contexts;
    }

    public List<MavenContext> getList(Node<MavenContext> node, State... states) {
        Assert.notNull(states, "states are required");
        List<MavenContext> contexts = new ArrayList<MavenContext>();
        for (Node<MavenContext> element : node.getBreadthFirstList()) {
            MavenContext context = element.getObject();
            State elementState = context.getState();
            if (isMatch(elementState, states)) {
                contexts.add(context);
            }
        }
        return contexts;
    }

    public Map<String, MavenContext> getMap(List<MavenContext> contexts) {
        Map<String, MavenContext> map = new HashMap<String, MavenContext>();
        for (MavenContext context : contexts) {
            map.put(context.getArtifactIdentifier(), context);
        }
        return map;
    }

    public Map<String, MavenContext> getPartialIdMap(List<MavenContext> contexts) {
        Map<String, MavenContext> map = new HashMap<String, MavenContext>();
        for (MavenContext context : contexts) {
            map.put(getPartialArtifactId(context.getArtifact()), context);
        }
        return map;
    }

    protected <T> void show(String label, List<T> list) {
        logger.info(label);
        for (T element : list) {
            logger.info(element.toString());
        }
    }

    protected GraphNode getGraphNode(DependencyNode dn) {
        Artifact a = dn.getArtifact();
        GraphNode n = new GraphNode();
        n.setId(counter.increment());
        n.setLabel(graphHelper.getLabel(a));
        String fillcolor = dn.getParent() == null ? ROOT_FILL_COLOR : n.getFillcolor();
        n.setFillcolor(fillcolor);
        return n;
    }

    public List<GraphNode> getGraphNodes(Node<MavenContext> node) {
        List<GraphNode> nodes = new ArrayList<GraphNode>();
        List<Node<MavenContext>> treeNodes = node.getBreadthFirstList();
        for (Node<MavenContext> treeNode : treeNodes) {
            nodes.add(treeNode.getObject().getGraphNode());
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
            // Using a dotted line for optional makes it clear enough
            // labelTokens.add(OPTIONAL);
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

    public String getProperty(Object bean, String name) {
        try {
            return BeanUtils.getProperty(bean, name);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.copyProperty(bean, name, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
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

    protected String lpad(Object o, int count) {
        String s = Helper.toEmpty(o);
        return StringUtils.leftPad(s, count);
    }

}
