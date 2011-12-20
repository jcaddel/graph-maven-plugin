package org.kuali.maven.plugins.graph.pojo;

import java.io.File;

public class DotContext {
    public boolean isKeepDotFile() {
        return keepDotFile;
    }

    public void setKeepDotFile(boolean keepDotFile) {
        this.keepDotFile = keepDotFile;
    }

    String executable = "dot";
    String type;
    File dotFile;
    File graph;
    boolean keepDotFile;

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getDotFile() {
        return dotFile;
    }

    public void setDotFile(File dotFile) {
        this.dotFile = dotFile;
    }

    public File getGraph() {
        return graph;
    }

    public void setGraph(File graph) {
        this.graph = graph;
    }
}
