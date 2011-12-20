package org.kuali.maven.plugins.graph.tree;

import org.apache.commons.collections15.list.SetUniqueList;

public class TreeMetaData {
    int size;

    SetUniqueList<String> artifactIdentifiers = Helper.decorate();
    SetUniqueList<String> partialArtifactIdentifiers = Helper.decorate();

    Tracker scopes = new Tracker();
    Tracker requiredness = new Tracker();
    Tracker states = new Tracker();
    Tracker groupIds = new Tracker();
    Tracker artifactIds = new Tracker();
    Tracker types = new Tracker();
    Tracker classifiers = new Tracker();
    Tracker versions = new Tracker();

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SetUniqueList<String> getArtifactIdentifiers() {
        return artifactIdentifiers;
    }

    public void setArtifactIdentifiers(SetUniqueList<String> artifactIdentifiers) {
        this.artifactIdentifiers = artifactIdentifiers;
    }

    public SetUniqueList<String> getPartialArtifactIdentifiers() {
        return partialArtifactIdentifiers;
    }

    public void setPartialArtifactIdentifiers(SetUniqueList<String> partialArtifactIdentifiers) {
        this.partialArtifactIdentifiers = partialArtifactIdentifiers;
    }

    public Tracker getScopes() {
        return scopes;
    }

    public void setScopes(Tracker scopes) {
        this.scopes = scopes;
    }

    public Tracker getRequiredness() {
        return requiredness;
    }

    public void setRequiredness(Tracker requiredness) {
        this.requiredness = requiredness;
    }

    public Tracker getStates() {
        return states;
    }

    public void setStates(Tracker states) {
        this.states = states;
    }

    public Tracker getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Tracker groupIds) {
        this.groupIds = groupIds;
    }

    public Tracker getArtifactIds() {
        return artifactIds;
    }

    public void setArtifactIds(Tracker artifactIds) {
        this.artifactIds = artifactIds;
    }

    public Tracker getTypes() {
        return types;
    }

    public void setTypes(Tracker types) {
        this.types = types;
    }

    public Tracker getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(Tracker classifiers) {
        this.classifiers = classifiers;
    }

    public Tracker getVersions() {
        return versions;
    }

    public void setVersions(Tracker versions) {
        this.versions = versions;
    }

}
