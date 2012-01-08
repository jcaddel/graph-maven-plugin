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
package org.kuali.maven.plugins.graph.mojo;

import java.io.File;

import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.util.Helper;

/**
 * <p>
 * This mojo produces customizable graphs of Maven dependency trees.
 * </p>
 *
 * <p>
 * By default, the complete dependency tree (including transitive dependencies) is graphed. Graphs can be filtered with
 * include/exclude criteria for artifacts and with show/hide criteria for Maven <code>scope</code> and
 * <code>optional</code> settings.
 * </p>
 *
 * <p>
 * Two layout styles are supported - <code>LINKED</code> and <code>FLAT</code>.
 * </p>
 *
 * <p>
 * In <code>LINKED</code> mode, the relationships between shared dependencies are shown. Each dependency included in the
 * build is displayed on the graph only once. The connections between dependencies are presented by Graphviz algorithms
 * as a directed hierarchical graph.
 * </p>
 *
 * <p>
 * For a transitive dependency, <code>LINKED</code> mode illustrates why Maven includes it in the build.
 * </p>
 *
 * <p>
 * For a shared dependency (eg commons-logging), <code>LINKED</code> mode shows what other libraries depend on it.
 * <code>LINKED</code> mode also shows the decisions Maven makes when resolving conflicts between pom's that depend on
 * different versions of the same artifact.
 * </p>
 *
 * <p>
 * In <code>FLAT</code> mode, dependencies are displayed exactly how they are defined in the pom's. This style can make
 * it easier to comprehend the dependency tree but relationships between shared dependencies are not drawn.
 * </p>
 *
 * @goal dependencies
 * @requiresDependencyResolution compile|test|runtime
 */
public class DependenciesMojo extends FilteredGraphMojo {

    /**
     * <p>
     * The file the graph will be written to. The file extension can be any output format supported by Graphviz (png,
     * jpg, gif, pdf, ...)
     * </p>
     *
     * @required
     * @parameter expression="${graph.file}" default-value="${project.build.directory}/graph/dependencies.png"
     */
    private File file;

    @Override
    public void execute() {
        MojoContext mc = Helper.copyProperties(MojoContext.class, this);
        GraphDescriptor gc = Helper.copyProperties(GraphDescriptor.class, this);
        MojoHelper mh = new MojoHelper();
        GraphDescriptor gd = mh.execute(mc, gc);
        if (gd == null) {
            getLog().info("No dependencies to graph");
        }

    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}