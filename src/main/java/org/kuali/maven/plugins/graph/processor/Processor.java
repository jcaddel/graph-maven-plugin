package org.kuali.maven.plugins.graph.processor;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;

public interface Processor {

    void process(Node<MavenContext> node);

}
