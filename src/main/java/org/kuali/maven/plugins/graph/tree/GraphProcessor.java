package org.kuali.maven.plugins.graph.tree;

import org.kuali.maven.plugins.graph.pojo.MavenContext;

public interface GraphProcessor {

    void process(Node<MavenContext> node);
}
