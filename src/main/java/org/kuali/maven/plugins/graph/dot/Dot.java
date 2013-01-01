/**
 * Copyright 2011-2013 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.dot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute the "dot" command for drawing graphs
 */
public class Dot {
    private static final Logger logger = LoggerFactory.getLogger(Dot.class);
    public static final int SUCCESS = 0;

    public void fillInContext(GraphDescriptor gc, String content) {
        File dotFile = createDotFile(gc.getFile(), content);
        String type = getType(gc.getFile());
        gc.setDotFile(dotFile);
        gc.setOutputFormat(type);
    }

    protected String[] getArgs(GraphDescriptor context) {
        List<String> args = new ArrayList<String>();
        args.add("-T" + context.getOutputFormat());
        args.add("-o" + context.getFile().getAbsolutePath());
        args.add(context.getDotFile().getAbsolutePath());
        return args.toArray(new String[args.size()]);
    }

    protected Commandline getCommandLine(GraphDescriptor context) {
        Commandline commandline = new Commandline();
        try {
            commandline.addSystemEnvironment();
        } catch (Exception ignore) {
            ; // ignore
        }
        commandline.setExecutable(context.getExecutable());
        commandline.addArguments(getArgs(context));
        return commandline;
    }

    protected int execute(Commandline commandLine, GraphDescriptor context) {
        try {
            StreamConsumer stdout = new DefaultConsumer();
            StreamConsumer stderr = new DefaultConsumer();
            int exitValue = CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
            if (exitValue != 0) {
                if (context.getIgnoreDotFailure()) {
                    logger.info("Ignoring failure of the 'dot' binary. Exit value=" + exitValue);
                } else {
                    throw new GraphException(getErrorMessage(commandLine, exitValue));
                }
            } else {
                // Log the name of the image that was created
                logger.debug(context.getFile().getPath());
            }
            return exitValue;
        } catch (CommandLineException e) {
            throw new GraphException(e);
        }
    }

    protected String getErrorMessage(Commandline commandLine, int exitValue) {
        String[] args = commandLine.getArguments();
        String executable = commandLine.getExecutable();
        String s = executable;
        for (String arg : args) {
            s = s + " " + arg;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Failed execution '" + s + "'");
        sb.append(" Exit value: '" + exitValue + "'");
        sb.append(" Is it installed? See: http://www.graphviz.org");
        return sb.toString();
    }

    public int execute(GraphDescriptor context) {
        int exitValue = -1;
        if (context.getExecuteDot()) {
            Commandline commandline = getCommandLine(context);
            exitValue = execute(commandline, context);
        } else {
            logger.info("Skip executing 'dot'");
        }
        if (!context.getKeepDotFile()) {
            context.getDotFile().delete();
        } else {
            logger.debug(context.getDotFile().getPath());
        }
        return exitValue;
    }

    protected File createDotFile(File graph, String content) {
        File dotFile = getDotFile(graph);
        try {
            FileUtils.write(dotFile, content);
            return dotFile;
        } catch (IOException e) {
            throw new GraphException(e);
        }
    }

    protected String getType(File graph) {
        return FilenameUtils.getExtension(graph.getName());
    }

    protected File getDotFile(File graph) {
        File dir = graph.getParentFile();
        String path = dir.getAbsolutePath();
        String basename = FilenameUtils.getBaseName(graph.getName());
        String extension = "dot";
        String filename = path + File.separator + basename + "." + extension;
        return new File(filename);
    }

}
