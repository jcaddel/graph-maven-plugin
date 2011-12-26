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
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.GraphContext;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.tree.Helper;

/**
 * Convenience mojo for generating multiple graphs.
 *
 * @goal multi
 * @requiresDependencyResolution compile|test|runtime
 */
public class MultiMojo extends BaseGraphMojo {

    /**
     * <p>
     * List of graph descriptors.
     * </p>
     *
     * @parameter
     */
    List<GraphContext> descriptors;

    /**
     * <p>
     * The directory graphs are generated into.
     * </p>
     *
     * @parameter expression="${graph.dir}" default-value="${project.build.directory}/graph"
     * @required
     */
    File outputDir;

    /**
     * <p>
     * The type of image files to produce
     * </p>
     *
     * @parameter expression="${graph.type}" default-value="png"
     * @required
     */
    String type;

    /**
     * <p>
     * If true, the default set of graphs is generated in addition to those provided in the <code>descriptors</code>
     * list.
     * </p>
     *
     * @parameter expression="${graph.useDefaultDescriptors}" default-value="true"
     * @required
     */
    boolean useDefaultDescriptors;

    @Override
    public void execute() {
        MojoContext mc = Helper.copyProperties(MojoContext.class, this);
        GraphContext gc = Helper.copyProperties(GraphContext.class, this);
        MojoHelper helper = new MojoHelper();
        helper.execute(mc, gc, descriptors);
    }

    public List<GraphContext> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<GraphContext> descriptors) {
        this.descriptors = descriptors;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File dir) {
        this.outputDir = dir;
    }

    public boolean isUseDefaultDescriptors() {
        return useDefaultDescriptors;
    }

    public void setUseDefaultDescriptors(boolean useDefaultDescriptors) {
        this.useDefaultDescriptors = useDefaultDescriptors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}