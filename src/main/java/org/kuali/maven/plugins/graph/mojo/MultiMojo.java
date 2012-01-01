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
import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Category;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.util.Helper;

/**
 * <p>
 * Convenience mojo for generating multiple graphs.
 * </p>
 *
 * @goal multi
 * @requiresDependencyResolution compile|test|runtime
 */
public class MultiMojo extends BaseGraphMojo {

    /**
     * <p>
     * List of graphs to generate organized into categories and rows.
     * </p>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>
     * &lt;categories&gt;
     *   &lt;category&gt;
     *     &lt;name&gt;logging&lt;/name&gt;
     *     &lt;description&gt;Dependencies on logging libraries&lt;/description&gt;
     *     &lt;rows&gt;
     *       &lt;row&gt;
     *         &lt;name&gt;logging&lt;/name&gt;
     *         &lt;description&gt;Dependencies on logging libraries&lt;/description&gt;
     *         &lt;descriptors&gt;
     *           &lt;descriptor&gt;
     *             &lt;includes&gt;org.slf4j,log4j&lt;/includes&gt;
     *             &lt;name&gt;other-logging&lt;/name&gt;
     *           &lt;/descriptor&gt;
     *           &lt;descriptor&gt;
     *             &lt;includes&gt;commons-logging&lt;/includes&gt;
     *             &lt;filterType&gt;PATH&lt;/filterType&gt;
     *             &lt;name&gt;commons-logging&lt;/name&gt;
     *           &lt;/descriptor&gt;
     *         &lt;/descriptors&gt;
     *       &lt;/row&gt;
     *     &lt;/rows&gt;
     *   &lt;/category&gt;
     * &lt;/categories&gt;
     * </pre>
     *
     * @parameter
     */
    List<Category> categories;

    /**
     * <p>
     * The directory graphs are generated into.
     * </p>
     *
     * @parameter expression="${graph.outputDir}" default-value="${project.build.directory}/graph"
     * @required
     */
    File outputDir;

    /**
     * <p>
     * The output format for the graph. This can be any format supported by Graphviz (png, jpg, gif, pdf, ...)
     * </p>
     *
     * @parameter expression="${graph.outputFormat}" default-value="png"
     * @required
     */
    String outputFormat;

    /**
     * <p>
     * If true, the default set of graphs is generated in addition to those provided in <code>categories</code> list.
     * </p>
     *
     * @parameter expression="${graph.generateDefaultGraphs}" default-value="true"
     */
    boolean generateDefaultGraphs;

    @Override
    public void execute() {
        MojoContext mc = Helper.copyProperties(MojoContext.class, this);
        GraphDescriptor gc = Helper.copyProperties(GraphDescriptor.class, this);
        MojoHelper helper = new MojoHelper();
        categories = Helper.isEmpty(categories) ? new ArrayList<Category>() : categories;
        helper.categories(mc, gc, categories);
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File dir) {
        this.outputDir = dir;
    }

    public boolean isGenerateDefaultGraphs() {
        return generateDefaultGraphs;
    }

    public void setGenerateDefaultGraphs(boolean generateDefaultGraphs) {
        this.generateDefaultGraphs = generateDefaultGraphs;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String type) {
        this.outputFormat = type;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}