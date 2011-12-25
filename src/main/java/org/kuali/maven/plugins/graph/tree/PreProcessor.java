package org.kuali.maven.plugins.graph.tree;

import org.kuali.maven.plugins.graph.GraphContext;
import org.kuali.maven.plugins.graph.pojo.MavenContext;

public interface PreProcessor {

    public void process(GraphContext context, Node<MavenContext> node);

}
