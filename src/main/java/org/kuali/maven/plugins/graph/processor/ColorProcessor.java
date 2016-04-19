package org.kuali.maven.plugins.graph.processor;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.join;
import static org.kuali.maven.plugins.graph.processor.ColorRuleExtractor.ColorPatternTarget;
import static org.kuali.maven.plugins.graph.processor.ColorRuleExtractor.ColorRule;

public class ColorProcessor implements Processor {

    public static final String ROOT_FILL_COLOR = "#dddddd"; // abloesen und durch default-Farbe.darker ersetzen

    private static final Logger logger = LoggerFactory.getLogger(ColorProcessor.class);

    String defaultColor;
    List<ColorRule> colorRules = new ArrayList<ColorRule>();

    public ColorProcessor(GraphDescriptor graphDescriptor) {
        defaultColor = graphDescriptor.getDefaultColor();
        colorRules = new ColorRuleExtractor().getColorRules(graphDescriptor.getColorRules());
    }

    @Override
    public void process(Node<MavenContext> node) {
        for (Node<MavenContext> element : node.getBreadthFirstList()) {
            updateGraphNodeColor(element.getObject());
        }
    }

    public void updateGraphNodeColor(MavenContext context) {
        DependencyNode dn = context.getDependencyNode();
        GraphNode n = context.getGraphNode();
        n.setFillcolor(getFillColor(dn, n));
    }

    private String getFillColor(DependencyNode dn, GraphNode n) {
        if (dn.getParent() == null) {
            return ROOT_FILL_COLOR;
        }
        List<String> colors = new ArrayList<String>();
        for (ColorRule colorRule : colorRules) {
            Artifact artifact = dn.getArtifact();
            if (getTargetValue(colorRule.target, artifact).contains(colorRule.pattern)) {
                colors.add(colorRule.color);
            }
        }
        if (colors.isEmpty()) {
            return defaultColor;
        }
        return join(colors, ":");
    }

    private String getTargetValue(ColorPatternTarget target, Artifact artifact) {
        switch (target) {
            case ARTIFACT:
                return artifact.getArtifactId();
            case GROUP:
                return artifact.getGroupId();
            case VERSION:
                return artifact.getVersion();
            case SCOPE:
                return artifact.getScope();
            case TYPE:
                return artifact.getType();
            case CLASSIFIER:
                return artifact.getClassifier();
        }
        throw new IllegalStateException("target=" + target + " not supported");
    }
}
