package org.kuali.maven.plugins.graph.pojo;

import java.util.List;

public class Folder {
    String name;
    Folder parent;
    List<Folder> folders;
    List<GraphDescriptor> descriptors;

    public Folder() {
        this(null);
    }

    public Folder(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<GraphDescriptor> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<GraphDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

}
