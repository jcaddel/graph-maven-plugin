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

/**
 * <p>
 * </p>
 *
 */
public abstract class BaseGraphMojo extends BaseMavenMojo {

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
     * Set to true to retain the .dot text file Graphviz uses to draw the graph
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
     * If true, any criteria used to filter the dependency tree are shown as a legend. Set this to false to prevent
     * filter criteria from being shown.
     * </p>
     *
     * @parameter expression="${graph.showLegend}" default-value="true"
     */
    private boolean showLegend;

    /**
     * <p>
     * Set this to false to render the graph without a title.
     * </p>
     *
     * @parameter expression="${graph.showTitle}" default-value="true"
     */
    private boolean showTitle;

    /**
     * <p>
     * Set this to false to skip executing the "dot" binary that produces an image from the .dot text file
     * </p>
     *
     * @parameter expression="${graph.executeDot}" default-value="true"
     */
    private boolean executeDot;

    /**
     * <p>
     * Set this to true to prevent the overall Maven build from failing if the "dot" binary returns a non-zero exit
     * value.
     * </p>
     *
     * @parameter expression="${graph.ignoreDotFailure}" default-value="false"
     */
    private boolean ignoreDotFailure;

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

}