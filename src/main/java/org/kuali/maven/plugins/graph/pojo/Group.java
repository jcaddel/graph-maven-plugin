package org.kuali.maven.plugins.graph.pojo;

import java.util.List;

public class Group {
    Category category;
    String name;
    String description;
    List<GraphDescriptor> descriptors;

    public Group() {
        this(null);
    }

    public Group(String name) {
        this(name, null);
    }

    public Group(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String label) {
        this.name = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GraphDescriptor> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<GraphDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
