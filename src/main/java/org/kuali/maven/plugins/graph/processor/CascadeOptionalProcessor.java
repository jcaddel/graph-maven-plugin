package org.kuali.maven.plugins.graph.processor;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CascadeOptionalProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(CascadeOptionalProcessor.class);
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> node) {
        boolean optional = node.getObject().isOptional();
        if (optional) {
            cascadeOptional(node);
        } else {
            for (Node<MavenContext> child : node.getChildren()) {
                process(child);
            }
        }
    }

    protected void cascadeOptional(Node<MavenContext> node) {
        for (Node<MavenContext> child : node.getChildren()) {
            MavenContext context = child.getObject();
            boolean optional = context.isOptional();
            if (!optional) {
                logger.debug("setting optional=true {} state={}", context.getArtifactIdentifier(), context.getState());
            }
            child.getObject().setOptional(true);
            cascadeOptional(child);
        }
    }
}
