package org.kuali.maven.plugins.graph.pojo;

public class Hider {
    boolean hideGroupId;
    boolean hideArtifactId;
    boolean hideVersion;
    boolean hideType;
    boolean hideClassifier;

    public boolean isHideGroupId() {
        return hideGroupId;
    }

    public void setHideGroupId(boolean groupId) {
        this.hideGroupId = groupId;
    }

    public boolean isHideArtifactId() {
        return hideArtifactId;
    }

    public void setHideArtifactId(boolean artifactId) {
        this.hideArtifactId = artifactId;
    }

    public boolean isHideVersion() {
        return hideVersion;
    }

    public void setHideVersion(boolean version) {
        this.hideVersion = version;
    }

    public boolean isHideType() {
        return hideType;
    }

    public void setHideType(boolean type) {
        this.hideType = type;
    }

    public boolean isHideClassifier() {
        return hideClassifier;
    }

    public void setHideClassifier(boolean hideClassifier) {
        this.hideClassifier = hideClassifier;
    }
}
