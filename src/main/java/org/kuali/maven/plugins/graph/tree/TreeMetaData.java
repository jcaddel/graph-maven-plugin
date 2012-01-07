/**
 * Copyright 2011-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.plugins.graph.tree;

import org.apache.commons.collections15.list.SetUniqueList;
import org.kuali.maven.plugins.graph.util.Helper;
import org.kuali.maven.plugins.graph.util.Tracker;

/**
 * <p>
 * Metadata about a Maven dependency tree
 * </p>
 *
 * @author jeffcaddel
 */
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
