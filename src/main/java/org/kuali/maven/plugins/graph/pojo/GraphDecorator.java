/**
 * Copyright 2010-2012 The Kuali Foundation
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

public class GraphDecorator {

    String label = "";
    String labeljust = "l";
    String labelloc = "t";
    String fontsize = "18";
    String fontname = "Helvetica";
    String ranksep = "1";
    String rankdir = Direction.DEFAULT_DIRECTION.name();
    String nodesep = ".05";

    public GraphDecorator() {
        super();
    }

    public GraphDecorator(String label, String rankdir) {
        super();
        this.label = label;
        this.rankdir = rankdir;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabeljust() {
        return labeljust;
    }

    public void setLabeljust(String labeljust) {
        this.labeljust = labeljust;
    }

    public String getLabelloc() {
        return labelloc;
    }

    public void setLabelloc(String labelloc) {
        this.labelloc = labelloc;
    }

    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getFontname() {
        return fontname;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }

    public String getRanksep() {
        return ranksep;
    }

    public void setRanksep(String ranksep) {
        this.ranksep = ranksep;
    }

    public String getRankdir() {
        return rankdir;
    }

    public void setRankdir(String rankdir) {
        this.rankdir = rankdir;
    }

    public String getNodesep() {
        return nodesep;
    }

    public void setNodesep(String nodesep) {
        this.nodesep = nodesep;
    }
}
