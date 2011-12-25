package org.kuali.maven.plugins.graph.mojo;

import java.io.File;
import java.util.Locale;

import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.kuali.maven.plugins.graph.pojo.Scope;

/**
 * @goal report
 * @requiresDependencyResolution compile|test|runtime
 */
public class ReportMojo extends BaseMojo implements MavenReport {

    private File file;

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
     * @parameter expression="${graph.imageType}" default-value="png"
     * @required
     */
    private String imageType;

    @SuppressWarnings("deprecation")
    @Override
    public void generate(Sink sink, Locale locale) throws MavenReportException {
        scopes(false);
        scopes(true);
    }

    protected void scopes(boolean transitive) {
        setTransitive(transitive);
        Scope[] scopes = Scope.values();
        String dir = transitive ? "transitive" : "direct";
        for (Scope scope : scopes) {
            setFile(new File(reportOutputDirectory, "/images/dependencies/" + dir + "/" + scope + "." + imageType));
            setShow(scope.toString());
            getLog().info(file.getPath());
            execute();
        }
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

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
