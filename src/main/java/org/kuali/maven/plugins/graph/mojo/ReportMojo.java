package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
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
     * part of a site generation, the output directory configured in the Maven Site Plugin will be used instead.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     */
    private File reportOutputDirectory;

    /**
     * Path relative to the output folder where images are created.
     *
     * @parameter expression="${graph.imagesPath}" default-value="graphs"
     * @required
     */
    private String graphsDir;

    @Override
    public void generate(Sink sink, Locale locale) throws MavenReportException {
        setOutputDir(new File(reportOutputDirectory + FS + graphsDir));
        MojoContext mc = Helper.copyProperties(MojoContext.class, this);
        GraphDescriptor gc = Helper.copyProperties(GraphDescriptor.class, this);
        MojoHelper helper = new MojoHelper();
        List<GraphDescriptor> executedDescriptors = helper.execute(mc, gc, descriptors);
        doHead(sink);
        doBody(sink, executedDescriptors);
        sink.flush();
        sink.close();
    }

    protected void doBody(Sink sink, List<GraphDescriptor> descriptors) {
        sink.body();
        sink.section1();
        sink.sectionTitle1();
        sink.text("Project Dependency Graphs");
        sink.sectionTitle1_();
        doSection2(sink, descriptors);
        sink.section1_();
        sink.body_();
    }

    protected void doSection2(Sink sink, List<GraphDescriptor> descriptors) {
        sink.section2();
        sink.sectionTitle2();
        sink.text("direct");
        sink.sectionTitle2_();
        for (GraphDescriptor d : descriptors) {
            sink.list();
            sink.listItem();
            sink.link(graphsDir + "/" + d.getCategory() + "/" + d.getLabel() + "." + d.getOutputFormat());
            sink.text(d.getLabel());
            sink.link_();
            sink.listItem_();
            sink.list_();
        }
        sink.section2_();
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

    public String getGraphsDir() {
        return graphsDir;
    }

    public void setGraphsDir(String imagesPath) {
        this.graphsDir = imagesPath;
    }

}
