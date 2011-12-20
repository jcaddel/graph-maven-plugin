package org.kuali.maven.plugins.graph.pojo;

public class GraphDecorator {
    String label = "Dependency Graph";
    String labeljust = "l";
    String labelloc = "t";
    String fontsize = "18";
    String fontname = "Serif";
    String ranksep = "1";
    String rankdir = Direction.DEFAULT_DIRECTION.name();
    String nodesep = ".05";

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
