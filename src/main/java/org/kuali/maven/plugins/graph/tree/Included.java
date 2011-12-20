package org.kuali.maven.plugins.graph.tree;

import java.util.Map;

import org.kuali.maven.plugins.graph.pojo.MavenContext;

public class Included {
    Map<String, MavenContext> ids;
    Map<String, MavenContext> partialIds;

    public Map<String, MavenContext> getIds() {
        return ids;
    }

    public void setIds(Map<String, MavenContext> ids) {
        this.ids = ids;
    }

    public Map<String, MavenContext> getPartialIds() {
        return partialIds;
    }

    public void setPartialIds(Map<String, MavenContext> partialIds) {
        this.partialIds = partialIds;
    }

}
