/**
 * Copyright (C) 2009 Progress Software, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.plugins.graph;

import java.io.File;

import org.kuali.maven.plugins.graph.dot.edge.EdgeHandler;
import org.kuali.maven.plugins.graph.dot.edge.FlatEdgeHandler;

/**
 * @goal flat
 * @requiresDependencyResolution compile|test|runtime
 */
public class FlatMojo extends BaseMojo {

    /**
     * The file the graph will be written to
     *
     * @parameter expression="${graph.file}" default-value="${project.build.directory}/graph/flat.png"
     */
    private File file;

    @Override
    protected EdgeHandler getEdgeHandler() {
        return new FlatEdgeHandler();
    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}