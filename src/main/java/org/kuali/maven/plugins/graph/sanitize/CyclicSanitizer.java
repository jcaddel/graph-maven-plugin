package org.kuali.maven.plugins.graph.sanitize;

import java.util.Map;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyclicSanitizer extends MavenContextSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(CyclicSanitizer.class);

    public CyclicSanitizer() {
        this(null);
    }

    public CyclicSanitizer(Map<String, MavenContext> included) {
        super(included, State.CYCLIC);
    }

    @Override
    protected void sanitize(MavenContext context, Map<String, MavenContext> included) {
        // Anything we need to do here?
        String id = TreeHelper.getArtifactId(context.getDependencyNode().getArtifact());
        logger.warn("cyclic->" + id);
    }

}
