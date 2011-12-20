/**
 * Copyright 2004-2011 The Kuali Foundation
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
