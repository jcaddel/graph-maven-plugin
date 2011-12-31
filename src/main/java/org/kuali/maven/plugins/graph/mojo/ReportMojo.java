package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.kuali.maven.plugins.graph.pojo.Category;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.Group;
import org.kuali.maven.plugins.graph.pojo.MojoContext;
import org.kuali.maven.plugins.graph.util.Helper;

/**
 * <p>
 * Generate a set of common dependency graphs and a report linking to them during Maven site generation.
 * </p>
 *
 * @goal report
 * @requiresDependencyResolution compile|test|runtime
 */
@SuppressWarnings("deprecation")
public class ReportMojo extends MultiMojo implements MavenReport {
    private static final String FS = System.getProperty("file.separator");

    /**
     * <p>
     * Output folder where the main page of the report will be generated. Note that this parameter is only relevant if
     * the goal is run directly from the command line or from the default lifecycle. If the goal is run indirectly as
     * part of site generation, the output directory configured in the Maven Site Plugin will be used instead.
     * </p>
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     */
    private File reportOutputDirectory;

    /**
     * Directory inside the output folder where graphs are created.
     *
     * @parameter expression="${graph.subDirectory}" default-value="graph"
     * @required
     */
    private String subDirectory;

    @Override
    public void generate(Sink sink, Locale locale) throws MavenReportException {
        setOutputDir(new File(reportOutputDirectory + FS + subDirectory));
        MojoContext mc = Helper.copyProperties(MojoContext.class, this);
        GraphDescriptor gd = Helper.copyProperties(GraphDescriptor.class, this);
        MojoHelper helper = new MojoHelper();
        categories = Helper.isEmpty(categories) ? new ArrayList<Category>() : categories;
        helper.categories(mc, gd, categories);
        doHead(sink);
        doBody(sink, categories);
        sink.flush();
        sink.close();
    }

    protected void doBody(Sink sink, List<Category> categories) {
        sink.body();
        sink.section1();
        sink.sectionTitle1();
        sink.text("Project Dependency Graphs");
        sink.sectionTitle1_();
        if (isEmpty(categories)) {
            sink.text("No graphs to display");
        } else {
            for (Category category : categories) {
                doCategory(sink, category);
            }
        }
        sink.section1_();
        sink.body_();
    }

    protected void doCategory(Sink sink, Category category) {
        if (isEmpty(category)) {
            return;
        }
        sink.section2();
        sink.sectionTitle2();
        sink.text(category.getName());
        sink.sectionTitle2_();
        sink.text(category.getDescription());
        sink.lineBreak();
        sink.lineBreak();
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.text("Type");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Graphs");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Description");
        sink.tableHeaderCell_();
        sink.tableRow_();
        for (Group group : category.getGroups()) {
            doGroup(sink, group);
        }
        sink.table_();
        sink.section2_();
    }

    protected void doGroup(Sink sink, Group group) {
        if (isEmpty(group)) {
            return;
        }
        sink.tableRow();
        sink.tableCell();
        sink.text(group.getName());
        sink.tableCell_();
        sink.tableCell();
        doDescriptors(sink, group.getDescriptors());
        sink.tableCell_();
        sink.tableCell();
        sink.text(group.getDescription());
        sink.tableCell_();
        sink.tableRow_();
    }

    protected void doDescriptors(Sink sink, List<GraphDescriptor> gds) {
        for (int i = 0; i < gds.size(); i++) {
            if (i != 0) {
                sink.text(",");
                sink.nonBreakingSpace();
            }
            doLink(sink, gds.get(i));
        }
    }

    protected void doLink(Sink sink, GraphDescriptor gd) {
        MojoHelper helper = new MojoHelper();
        String href = subDirectory + "/" + helper.getRelativePath(gd);
        sink.link(href);
        sink.text(gd.getName());
        sink.link_();
    }

    protected void doHead(Sink sink) {
        sink.head();
        sink.title();
        sink.text("Project Dependency Graphs");
        sink.title_();
        sink.head_();
    }

    @Override
    public String getOutputName() {
        return "dependency-graphs";
    }

    @Override
    public String getCategoryName() {
        return CATEGORY_PROJECT_INFORMATION;
    }

    @Override
    public String getName(Locale locale) {
        return "Dependency Graphs";
    }

    @Override
    public String getDescription(Locale locale) {
        return "This document provides links to graphs that make it easier to visualize the project's dependencies";
    }

    @Override
    public boolean isExternalReport() {
        return false;
    }

    @Override
    public boolean canGenerateReport() {
        return true;
    }

    @Override
    public File getReportOutputDirectory() {
        return reportOutputDirectory;
    }

    @Override
    public void setReportOutputDirectory(File reportOutputDirectory) {
        this.reportOutputDirectory = reportOutputDirectory;
    }

    public String getSubDirectory() {
        return subDirectory;
    }

    public void setSubDirectory(String subDirectory) {
        this.subDirectory = subDirectory;
    }

    protected boolean isEmpty(List<Category> categories) {
        if (Helper.isEmpty(categories)) {
            return true;
        } else {
            for (Category category : categories) {
                if (!isEmpty(category)) {
                    return false;
                }
            }
            return true;
        }
    }

    protected boolean isEmpty(Category category) {
        if (Helper.isEmpty(category.getGroups())) {
            return true;
        } else {
            return isEmptyGroups(category.getGroups());
        }
    }

    protected boolean isEmptyGroups(List<Group> groups) {
        if (Helper.isEmpty(groups)) {
            return true;
        }
        for (Group group : groups) {
            if (!isEmpty(group)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isEmpty(Group group) {
        return Helper.isEmpty(group.getDescriptors());
    }

}
