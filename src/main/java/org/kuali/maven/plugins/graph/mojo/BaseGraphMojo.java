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
package org.kuali.maven.plugins.graph.mojo;

import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.FilterType;

/**
 * <p>
 * </p>
 *
 */
public abstract class BaseGraphMojo extends BaseMavenMojo {

    /**
     * <p>
     * Controls how filtering is applied to the tree. Valid options are HIDE, PATH.
     * </p>
     *
     * @parameter expression="${graph.filterType}" default-value="HIDE"
     */
    private FilterType filterType;

    /**
     * <p>
     * The title for the graph
     * </p>
     *
     * @parameter expression="${graph.title}" default-value="Dependency Graph for ${project.name}"
     */
    private String title;

    /**
     * <p>
     * If true, the .dot text file Graphviz uses to draw the graph is retained
     * </p>
     *
     * @parameter expression="${graph.keepDotFile}" default-value="false"
     */
    private boolean keepDotFile;

    /**
     * <p>
     * The direction for the graph layout. Valid values are TB, LR, BT, RL. Top to bottom, left to right, bottom to top,
     * and right to left, respectively.
     * </p>
     *
     * @required
     * @parameter expression="${graph.direction}" default-value="TB"
     */
    private Direction direction;

    /**
     * <p>
     * If true, artifact group id's are displayed on the graph.
     * </p>
     *
     * @parameter expression="${graph.showGroupIds}" default-value="true"
     */
    private boolean showGroupIds;

    /**
     * <p>
     * If true, duplicate dependencies are displayed on the graph.
     * </p>
     *
     * @parameter expression="${graph.showDuplicates}" default-value="false"
     */
    private boolean showDuplicates;

    /**
     * <p>
     * If true, dependency conflicts are displayed on the graph
     * </p>
     *
     * @parameter expression="${graph.showConflicts}" default-value="true"
     */
    private boolean showConflicts;

    /**
     * <p>
     * If true, any dependency marked as optional will have all of its transitive dependencies displayed as optional
     * also.
     * </p>
     *
     * @parameter expression="${graph.cascadeOptional}" default-value="true"
     */
    private boolean cascadeOptional;

    /**
     * <p>
     * If true, any filters applied to the dependency tree are shown in the legend.
     * </p>
     *
     * @parameter expression="${graph.showLegend}" default-value="true"
     */
    private boolean showLegend;

    /**
     * <p>
     * If true, the title for the graph will be displayed.
     * </p>
     *
     * @parameter expression="${graph.showTitle}" default-value="true"
     */
    private boolean showTitle;

    /**
     * <p>
     * If true, the Graphviz "dot" binary is executed to produce a graph from the .dot text file
     * </p>
     *
     * @parameter expression="${graph.executeDot}" default-value="true"
     */
    private boolean executeDot;

    /**
     * <p>
     * If true, the Maven build will continue even if the "dot" executable returns a non-zero exit value.
     * </p>
     *
     * @parameter expression="${graph.ignoreDotFailure}" default-value="false"
     */
    private boolean ignoreDotFailure;

    /**
     * <p>
     * If true, do not invoke Graphviz if there are no dependencies to graph.
     * </p>
     *
     * @parameter expression="${graph.skipEmptyGraphs}" default-value="true"
     */
    private boolean skipEmptyGraphs;

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

    public boolean isShowDuplicates() {
        return showDuplicates;
    }

    public void setShowDuplicates(boolean showDuplicates) {
        this.showDuplicates = showDuplicates;
    }

    public boolean isCascadeOptional() {
        return cascadeOptional;
    }

    public void setCascadeOptional(boolean cascadeOptional) {
        this.cascadeOptional = cascadeOptional;
    }

    public boolean isShowConflicts() {
        return showConflicts;
    }

    public void setShowConflicts(boolean showConflicts) {
        this.showConflicts = showConflicts;
    }

    public boolean isSkipEmptyGraphs() {
        return skipEmptyGraphs;
    }

    public void setSkipEmptyGraphs(boolean skipEmptyGraphs) {
        this.skipEmptyGraphs = skipEmptyGraphs;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

}