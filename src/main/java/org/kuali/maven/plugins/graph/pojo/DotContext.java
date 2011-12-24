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
package org.kuali.maven.plugins.graph.pojo;

import java.io.File;

public class DotContext {
    String executable = "dot";
    String type;
    File dotFile;
    File graph;
    boolean keepDotFile;
    boolean executeDot;
    boolean ignoreDotFailure;

    public boolean isKeepDotFile() {
        return keepDotFile;
    }

    public void setKeepDotFile(boolean keepDotFile) {
        this.keepDotFile = keepDotFile;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getDotFile() {
        return dotFile;
    }

    public void setDotFile(File dotFile) {
        this.dotFile = dotFile;
    }

    public File getGraph() {
        return graph;
    }

    public void setGraph(File graph) {
        this.graph = graph;
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

    public void setIgnoreDotFailure(boolean failIfDotFails) {
        this.ignoreDotFailure = failIfDotFails;
    }
}
