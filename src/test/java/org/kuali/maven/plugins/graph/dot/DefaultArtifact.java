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
package org.kuali.maven.plugins.graph.dot;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;

@SuppressWarnings("deprecation")
public class DefaultArtifact implements Artifact {

    String groupId;
    String artifactId;
    String classifier;
    String version;
    String type;
    String scope;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    @Override
    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean hasClassifier() {

        return false;
    }

    @Override
    public File getFile() {

        return null;
    }

    @Override
    public void setFile(File destination) {

    }

    @Override
    public String getBaseVersion() {

        return null;
    }

    @Override
    public void setBaseVersion(String baseVersion) {

    }

    @Override
    public String getId() {

        return null;
    }

    @Override
    public String getDependencyConflictId() {

        return null;
    }

    @Override
    public void addMetadata(ArtifactMetadata metadata) {

    }

    @Override
    public void setRepository(ArtifactRepository remoteRepository) {

    }

    @Override
    public ArtifactRepository getRepository() {

        return null;
    }

    @Override
    public void updateVersion(String version, ArtifactRepository localRepository) {

    }

    @Override
    public String getDownloadUrl() {

        return null;
    }

    @Override
    public void setDownloadUrl(String downloadUrl) {

    }

    @Override
    public ArtifactFilter getDependencyFilter() {

        return null;
    }

    @Override
    public void setDependencyFilter(ArtifactFilter artifactFilter) {

    }

    @Override
    public ArtifactHandler getArtifactHandler() {

        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setDependencyTrail(List dependencyTrail) {

    }

    @Override
    public VersionRange getVersionRange() {

        return null;
    }

    @Override
    public void setVersionRange(VersionRange newRange) {

    }

    @Override
    public void selectVersion(String version) {

    }

    @Override
    public boolean isSnapshot() {

        return false;
    }

    @Override
    public void setResolved(boolean resolved) {

    }

    @Override
    public boolean isResolved() {

        return false;
    }

    @Override
    public void setResolvedVersion(String version) {

    }

    @Override
    public void setArtifactHandler(ArtifactHandler handler) {

    }

    @Override
    public boolean isRelease() {

        return false;
    }

    @Override
    public void setRelease(boolean release) {

    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAvailableVersions(List versions) {

    }

    @Override
    public boolean isOptional() {

        return false;
    }

    @Override
    public void setOptional(boolean optional) {

    }

    @Override
    public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {

        return null;
    }

    @Override
    public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {

        return false;
    }

    @Override
    public int compareTo(Artifact arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<ArtifactVersion> getAvailableVersions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<ArtifactMetadata> getMetadataList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getDependencyTrail() {
        // TODO Auto-generated method stub
        return null;
    }
}
