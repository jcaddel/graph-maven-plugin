package org.kuali.maven.plugins.graph.tree;

import java.util.Map;

import org.kuali.maven.plugins.graph.pojo.MavenContext;

public class Included {
    Map<String, Node<MavenContext>> ids;
    Map<String, Node<MavenContext>> partialIds;

    public Map<String, Node<MavenContext>> getIds() {
        return ids;
    }

    public void setIds(Map<String, Node<MavenContext>> ids) {
        this.ids = ids;
    }

    public Map<String, Node<MavenContext>> getPartialIds() {
        return partialIds;
    }

    public void setPartialIds(Map<String, Node<MavenContext>> partialIds) {
        this.partialIds = partialIds;
    }

}
