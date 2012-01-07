/**
 * Copyright 2010-2012 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.LabelCount;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.tree.TreeMetaData;
import org.kuali.maven.plugins.graph.util.Helper;
import org.kuali.maven.plugins.graph.util.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Emit logging statements for metadata about the dependency tree
 */
public class ShowMetadataProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ShowMetadataProcessor.class);

    @Override
    public void process(Node<MavenContext> node) {
        TreeMetaData md = getMetaData(node);
        show(md);
    }

    protected void show(TreeMetaData md) {
        logger.info("Metadata for " + md.getSize() + " dependency nodes");
        logger.info("states -" + toString(md.getStates()));
        logger.info("requiredness -" + toString(md.getRequiredness()));
        logger.info("scopes -" + toString(md.getScopes()));
        logger.info("types -" + toString(md.getTypes()));
        logger.info("classifiers -" + toString(md.getClassifiers()));
        int groups = md.getGroupIds().size();
        int artifacts = md.getArtifactIds().size();
        int versions = md.getVersions().size();
        logger.info("unique gav info - groups:" + groups + " artifacts:" + artifacts + " versions:" + versions);
        logger.info("unique artifacts (including version): " + md.getArtifactIdentifiers().size());
        logger.info("unique artifacts  (ignoring version): " + md.getPartialArtifactIdentifiers().size());
    }

    public TreeMetaData getMetaData(Node<MavenContext> node) {
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        TreeMetaData metaData = new TreeMetaData();
        metaData.setSize(list.size());
        for (Node<MavenContext> element : list) {
            updateMetaData(metaData, element.getObject());
        }
        return metaData;
    }

    protected void updateMetaData(TreeMetaData md, MavenContext context) {
        DependencyNode dn = context.getDependencyNode();
        updateMetaData(md, dn.getArtifact(), context.isOptional());
        if (dn.getParent() != null) {
            md.getStates().increment(context.getState().getValue());
        }
    }

    protected void updateMetaData(TreeMetaData md, Artifact a, boolean optional) {
        md.getGroupIds().increment(a.getGroupId());
        md.getArtifactIds().increment(a.getArtifactId());
        md.getTypes().increment(a.getType());
        String classifier = a.getClassifier();
        if (!Helper.isBlank(classifier)) {
            md.getClassifiers().increment(classifier);
        }
        md.getVersions().increment(a.getVersion());
        Scope scope = Scope.getScope(a.getScope());
        if (scope != null) {
            md.getScopes().increment(scope.toString());
        }
        md.getRequiredness().increment(optional ? TreeHelper.OPTIONAL : TreeHelper.REQUIRED);
        md.getArtifactIdentifiers().add(TreeHelper.getArtifactId(a));
        md.getPartialArtifactIdentifiers().add(TreeHelper.getPartialArtifactId(a));
    }

    protected String toString(Tracker tracker) {
        List<LabelCount> labels = new ArrayList<LabelCount>();
        for (String key : tracker.keySet()) {
            labels.add(new LabelCount(key, tracker.get(key)));
        }
        Collections.sort(labels);
        Collections.reverse(labels);
        StringBuilder sb = new StringBuilder();
        for (LabelCount label : labels) {
            sb.append(" " + label.getLabel() + ":" + label.getCount());
        }
        return sb.toString();
    }

}
