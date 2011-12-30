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
 * Two layout styles are supported. In <code>LINKED</code> mode, relationships between shared dependencies are included
 * in the graph. In <code>FLAT</code> mode, dependencies are displayed exactly how they are defined in pom's.
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
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}