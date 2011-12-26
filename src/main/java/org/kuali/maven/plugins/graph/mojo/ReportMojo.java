package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
import java.util.Locale;

import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;

/**
 * @goal report
 * @requiresDependencyResolution compile|test|runtime
 */
@SuppressWarnings("deprecation")
public class ReportMojo extends MultiMojo implements MavenReport {

    /**
     * Output folder where the main page of the report will be generated. Note that this parameter is only relevant if
     * the goal is run directly from the command line or from the default lifecycle. If the goal is run indirectly as
     * part of a site generation, the output directory configured in the Maven Site Plugin will be used instead.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     */
    private File reportOutputDirectory;

    @Override
    public void generate(Sink sink, Locale locale) throws MavenReportException {
        setOutputDir(reportOutputDirectory);
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
        return "Maven dependency tree graphs";
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

}
