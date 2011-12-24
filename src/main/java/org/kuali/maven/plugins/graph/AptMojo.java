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
package org.kuali.maven.plugins.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @goal apt
 */
public class AptMojo extends AbstractMojo {

    /**
     * @required
     * @parameter expression="${graph.apt}" default-value="classpath:dependency-graphs.apt"
     */
    private String apt;

    /**
     * @required
     * @parameter expression="${graph.dest}" default-value="${project.basedir}/src/site/apt"
     */
    private File dest;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            copy(apt, getFilename(apt), dest);
        } catch (Exception e) {
            throw new MojoExecutionException("Unexpected error", e);
        }
    }

    protected void copy(String location, String filename, File dir) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            String path = dir.getAbsolutePath() + "/" + filename;
            File file = new File(path);
            out = FileUtils.openOutputStream(file);
            in = getInputStream(apt);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    protected String getFilename(String location) {
        File file = new File(apt);
        if (file.exists()) {
            return file.getName();
        }
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(apt);
        return resource.getFilename();
    }

    protected InputStream getInputStream(String apt) throws IOException {
        File file = new File(apt);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(apt);
        return resource.getInputStream();
    }

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
    }

}