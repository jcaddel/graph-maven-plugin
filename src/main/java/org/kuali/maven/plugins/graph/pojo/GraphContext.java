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
import java.util.List;

import org.kuali.maven.plugins.graph.dot.EdgeHandler;
import org.kuali.maven.plugins.graph.tree.PostProcessor;
import org.kuali.maven.plugins.graph.tree.PreProcessor;

/**
 *
 */
public class GraphContext {
    String executable = "dot";
    EdgeHandler edgeHandler;
    List<PreProcessor> preProcessors;
    List<PostProcessor> postProcessors;
    String title;
    boolean keepDotFile;
    boolean transitive;
    boolean skip;
    String includes;
    String excludes;
    String hide;
    String show;
    Direction direction;
    boolean showGroupIds;
    boolean showLegend;
    boolean showTitle;
    boolean executeDot;
    boolean ignoreDotFailure;
    boolean verbose;
    int depth;
    File file;
    String content;
    File dotFile;
    String type;
    LayoutStyle layout;

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

    public List<PreProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(List<PreProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public List<PostProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<PostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isKeepDotFile() {
        return keepDotFile;
    }

    public void setKeepDotFile(boolean keepDotFile) {
        this.keepDotFile = keepDotFile;
    }

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
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

    public boolean isShowGroupIds() {
        return showGroupIds;
    }

    public void setShowGroupIds(boolean showGroupIds) {
        this.showGroupIds = showGroupIds;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public boolean isExecuteDot() {
        return executeDot;
    }

    public void setExecuteDot(boolean executeDot) {
        this.executeDot = executeDot;
    }

    public boolean isIgnoreDotFailure() {
        return ignoreDotFailure;
    }

    public void setIgnoreDotFailure(boolean ignoreDotFailure) {
        this.ignoreDotFailure = ignoreDotFailure;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
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

    public void setLayout(LayoutStyle style) {
        this.layout = style;
    }

}