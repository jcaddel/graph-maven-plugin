package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
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
 * @goal report
 * @requiresDependencyResolution compile|test|runtime
 */
@SuppressWarnings("deprecation")
public class ReportMojo extends MultiMojo implements MavenReport {
    private static final String FS = System.getProperty("file.separator");

    /**
     * Output folder where the main page of the report will be generated. Note that this parameter is only relevant if
     * the goal is run directly from the command line or from the default lifecycle. If the goal is run indirectly as
     * part of site generation, the output directory configured in the Maven Site Plugin will be used instead.
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
        List<Category> categories = helper.getDefaultCategories(gd);
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
        for (Group group : category.getGroups()) {
            doGroup(sink, group);
        }
        sink.section2_();
    }

    protected void doGroup(Sink sink, Group group) {
        if (isEmpty(group)) {
            return;
        }
        sink.section3();
        sink.sectionTitle3();
        sink.text(group.getName());
        sink.sectionTitle3_();
        sink.text(group.getDescription());
        sink.list();
        sink.listItem();
        for (GraphDescriptor gd : group.getDescriptors()) {
            doDescriptor(sink, gd);
        }
        sink.listItem_();
        sink.list_();
        sink.section3_();
    }

    protected void doDescriptor(Sink sink, GraphDescriptor gd) {
        doLink(sink, gd);
        sink.text(",");
        sink.nonBreakingSpace();
    }

    protected void doLink(Sink sink, GraphDescriptor gd) {
        MojoHelper helper = new MojoHelper();
        String href = subDirectory + "/" + helper.getRelativePath(gd);
        String show = getShow(gd);
        sink.link(href);
        sink.text(show);
        sink.link_();
    }

    protected String getShow(GraphDescriptor gd) {
        return gd.getLayout().toString().toLowerCase();
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
