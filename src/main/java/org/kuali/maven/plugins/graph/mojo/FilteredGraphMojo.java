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

import java.io.File;

import org.kuali.maven.plugins.graph.pojo.Layout;

/**
 * <p>
 * </p>
 *
 */
public abstract class FilteredGraphMojo extends BaseGraphMojo {

    /**
     * <p>
     * If true, transitive dependencies are graphed.
     * </p>
     *
     * @parameter expression="${graph.transitive}" default-value="true"
     */
    private boolean transitive;

    /**
     * <p>
     * The style for the graph layout. Valid options are <code>LINKED</code> and <code>FLAT</code>
     * </p>
     *
     * <p>
     * In <code>LINKED</code> mode, the relationships between shared dependencies are shown. Each dependency included in
     * the build is displayed on the graph only once. The connections between dependencies are presented by Graphviz
     * algorithms as a directed hierarchical graph.
     * </p>
     *
     * <p>
     * For a transitive dependency, <code>LINKED</code> mode illustrates why Maven includes it in the build.
     * </p>
     *
     * <p>
     * For a shared dependency (eg commons-logging), <code>LINKED</code> mode shows what other libraries depend on it.
     * <code>LINKED</code> mode also shows the decisions Maven makes when resolving conflicts between pom's that depend
     * on different versions of the same artifact.
     * </p>
     *
     * <p>
     * In <code>FLAT</code> mode, dependencies are displayed exactly how they are defined in the pom's. This style can
     * make it easier to comprehend the dependency tree but relationships between shared dependencies are not drawn.
     * </p>
     *
     * @parameter expression="${graph.layout}" default-value="LINKED"
     * @required
     */
    private Layout layout;

    /**
     * <p>
     * Comma delimited list of patterns for including artifacts. The pattern syntax has the form -
     * [groupId]:[artifactId]:[type]:[classifier]:[version]
     * </p>
     *
     * <p>
     * Each pattern segment is optional and supports the use of the asterisk "*" as a wildcard. An empty pattern segment
     * is treated as a wildcard.
     * </p>
     *
     * <p>
     * If include patterns are provided, a dependency must match one of the include patterns or it (along with the
     * dependency tree beneath it) will be hidden.
     * </p>
     *
     * <p>
     * If not provided, all dependencies are included.
     * </p>
     *
     * <p>
     * Include patterns are overridden by exclude patterns.
     * </p>
     *
     * @parameter expression="${graph.includes}"
     */
    private String includes;

    /**
     * <p>
     * Comma delimited list of artifact patterns to exclude. The pattern syntax has the form -
     * [groupId]:[artifactId]:[type]:[classifier]:[version]
     * </p>
     *
     * <p>
     * Each pattern segment is optional and supports the use of the asterisk "*" as a wildcard. An empty pattern segment
     * is treated as a wildcard.
     * </p>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>
     * Exclude commons-logging     -Dgraph.excludes=commons-logging
     * Exclude logging in general  -Dgraph.excludes=commons-logging,org.slf4j,log4j
     * Exclude war files           -Dgraph.excludes=*:*:war
     * Exclude source              -Dgraph.excludes=:::sources
     * </pre>
     *
     * <p>
     * If exclude patterns are provided, a match with any exclude pattern will prevent a dependency (and the dependency
     * tree beneath it) from being displayed.
     * </p>
     *
     * <p>
     * Exclude patterns override include patterns.
     * </p>
     *
     * <p>
     * If not provided, no artifacts are excluded.
     * </p>
     *
     * @parameter expression="${graph.excludes}"
     */
    private String excludes;

    /**
     * <p>
     * Comma delimited list of patterns for hiding artifacts. The pattern syntax has the form -
     * [scope]:[optional|required]
     * </p>
     *
     * <p>
     * Scopes: compile,provided,runtime,test,system,import
     * </p>
     *
     * <p>
     * Each pattern segment is optional and supports the use of the asterisk "*" as a wildcard. An empty pattern segment
     * is treated as a wildcard.
     * </p>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>
     * Hide test dependencies             -Dgraph.hide=test
     * Hide test & provided dependencies  -Dgraph.hide=test,provided
     * Hide optional dependencies         -Dgraph.hide=*:optional
     * Hide required dependencies         -Dgraph.hide=*:required
     * </pre>
     *
     * <p>
     * If hide patterns are provided, a match with any hide pattern will prevent a dependency (and the dependency tree
     * beneath it) from being displayed.
     * </p>
     *
     * <p>
     * Hide patterns override show patterns.
     * </p>
     *
     * <p>
     * If not provided, no dependencies are hidden.
     * </p>
     *
     * @parameter expression="${graph.hide}"
     */
    private String hide;

    /**
     * <p>
     * Comma delimited list of patterns for showing artifacts. The pattern syntax has the form -
     * [scope]:[optional|required]
     * </p>
     *
     * <p>
     * Scopes: compile,provided,runtime,test,system,import
     * </p>
     *
     * <p>
     * Each pattern segment is optional and supports the use of the asterisk "*" as a wildcard. An empty pattern segment
     * is treated as a wildcard.
     * </p>
     *
     * <p>
     * If show patterns are provided a dependency must match one of the show patterns or it (along with the dependency
     * tree beneath it) will be hidden.
     * </p>
     *
     * <p>
     * Show patterns are overridden by hide patterns.
     * </p>
     *
     * <p>
     * If not provided, all artifacts are shown.
     * </p>
     *
     * @parameter expression="${graph.show}"
     */
    private String show;

    public abstract File getFile();

    /**
     * <p>
     * Restricts the depth of the dependency tree.
     * <p>
     *
     * <p>
     * To show only the dependencies of the current project, set this to 1. To show the dependencies of the current
     * project and their direct dependencies, set this to 2.
     * </p>
     *
     * @parameter expression="${graph.depth}" default-value="-1"
     */
    private int depth;

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

}