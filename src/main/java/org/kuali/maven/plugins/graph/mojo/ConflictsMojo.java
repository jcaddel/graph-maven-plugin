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

import org.kuali.maven.plugins.graph.pojo.LayoutStyle;
import org.kuali.maven.plugins.graph.tree.ConflictsProcessor;
import org.kuali.maven.plugins.graph.tree.PostProcessor;

/**
 * <p>
 * This mojo displays any dependencies where conflict resolution has taken place.
 * </p>
 *
 * <p>
 * Maven supports the resolution of artifact versions by way of nearest-wins. That is, for any set of dependencies
 * sharing the same [groupId]:[artifactId]:[type]:[classifier], the one declared nearest to the current project in the
 * dependency tree is selected for use.
 * </p>
 *
 * @goal conflicts
 * @requiresDependencyResolution compile|test|runtime
 */
public class ConflictsMojo extends BaseGraphMojo {

    /**
     * The file the graph will be written to
     *
     * @required
     * @parameter expression="${graph.file}" default-value="${project.build.directory}/graph/conflicts.png"
     */
    private File file;

    /**
     * This filter restricts the display to conflicts only.
     *
     * @parameter expression="${graph.conflictsFilter}" default-value="::conflict"
     */
    private String conflictsFilter;

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void execute() {
        setShow(getShow() == null ? conflictsFilter : getShow() + "," + conflictsFilter);
        setLayout(LayoutStyle.CONDENSED);
        List<PostProcessor> pps = new ArrayList<PostProcessor>(getPostProcessors());
        pps.add(new ConflictsProcessor());
        setPostProcessors(pps);
        super.execute();
    }

    public String getConflictsFilter() {
        return conflictsFilter;
    }

    public void setConflictsFilter(String conflictsFilter) {
        this.conflictsFilter = conflictsFilter;
    }

}