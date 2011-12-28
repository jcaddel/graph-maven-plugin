/**
 * Copyright 2010-2011 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.pojo;

public class LabelContext {
    boolean showGroupIds = true;
    boolean showArtifactIds = true;
    boolean showTypes = true;
    boolean showClassifiers = true;
    boolean showVersions = true;

    public boolean isShowGroupIds() {
        return showGroupIds;
    }

    public void setShowGroupIds(boolean showGroupIds) {
        this.showGroupIds = showGroupIds;
    }

    public boolean isShowArtifactIds() {
        return showArtifactIds;
    }

    public void setShowArtifactIds(boolean showArtifactIds) {
        this.showArtifactIds = showArtifactIds;
    }

    public boolean isShowTypes() {
        return showTypes;
    }

    public void setShowTypes(boolean showTypes) {
        this.showTypes = showTypes;
    }

    public boolean isShowClassifiers() {
        return showClassifiers;
    }

    public void setShowClassifiers(boolean showClassifiers) {
        this.showClassifiers = showClassifiers;
    }

    public boolean isShowVersions() {
        return showVersions;
    }

    public void setShowVersions(boolean showVersions) {
        this.showVersions = showVersions;
    }

}
