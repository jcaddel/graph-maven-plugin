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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kuali.maven.plugins.graph.dot.CondensedEdgeHandler;
import org.kuali.maven.plugins.graph.dot.EdgeHandler;
import org.kuali.maven.plugins.graph.tree.HideProcessor;
import org.kuali.maven.plugins.graph.tree.PostProcessor;
import org.kuali.maven.plugins.graph.tree.PreProcessor;

/**
 *
 */
public class GraphContextOriginal {
    public static final List<? extends PreProcessor> DEFAULT_PRE_PROCESSORS = Collections
            .singletonList(new HideProcessor());

    String executable = "dot";
    EdgeHandler edgeHandler = new CondensedEdgeHandler();
    List<? extends PreProcessor> preProcessors = new ArrayList<PreProcessor>(DEFAULT_PRE_PROCESSORS);
    List<? extends PostProcessor> postProcessors = new ArrayList<PostProcessor>();
    String title = "Dependency Graph";
    Boolean keepDotFile = false;
    Boolean transitive = true;
    String includes;
    String excludes;
    String hide;
    String show;
    Direction direction = Direction.TB;
    Boolean showGroupIds = true;
    Boolean showLegend = true;
    Boolean showTitle = true;
    Boolean executeDot = true;
    Boolean ignoreDotFailure = false;
    Integer depth = -1;
    File file;
    String content;
    File dotFile;
    String type = "png";
    LayoutStyle layout = LayoutStyle.CONDENSED;
    String label;
    String category;

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public EdgeHandler getEdgeHandler() {
        return edgeHandler;
    }

    public void setEdgeHandler(EdgeHandler edgeHandler) {
        this.edgeHandler = edgeHandler;
    }

    public List<? extends PreProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(List<? extends PreProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public List<? extends PostProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<? extends PostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getKeepDotFile() {
        return keepDotFile;
    }

    public void setKeepDotFile(Boolean keepDotFile) {
        this.keepDotFile = keepDotFile;
    }

    public Boolean getTransitive() {
        return transitive;
    }

    public void setTransitive(Boolean transitive) {
        this.transitive = transitive;
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Boolean getShowGroupIds() {
        return showGroupIds;
    }

    public void setShowGroupIds(Boolean showGroupIds) {
        this.showGroupIds = showGroupIds;
    }

    public Boolean getShowLegend() {
        return showLegend;
    }

    public void setShowLegend(Boolean showLegend) {
        this.showLegend = showLegend;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public Boolean getExecuteDot() {
        return executeDot;
    }

    public void setExecuteDot(Boolean executeDot) {
        this.executeDot = executeDot;
    }

    public Boolean getIgnoreDotFailure() {
        return ignoreDotFailure;
    }

    public void setIgnoreDotFailure(Boolean ignoreDotFailure) {
        this.ignoreDotFailure = ignoreDotFailure;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getDotFile() {
        return dotFile;
    }

    public void setDotFile(File dotFile) {
        this.dotFile = dotFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LayoutStyle getLayout() {
        return layout;
    }

    public void setLayout(LayoutStyle layout) {
        this.layout = layout;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}