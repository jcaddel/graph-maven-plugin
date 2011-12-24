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
import org.kuali.maven.plugins.graph.pojo.DotContext;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute the "dot" command for drawing graphs
 */
public class Dot {
    private static final Logger logger = LoggerFactory.getLogger(Dot.class);

    public DotContext getDotContext(File graph, String content, boolean keepDotFile, boolean executeDot,
            boolean ignoreDotFailure) {
        File dotFile = createDotFile(graph, content);
        String type = getType(graph);

        DotContext context = new DotContext();
        context.setGraph(graph);
        context.setDotFile(dotFile);
        context.setType(type);
        context.setKeepDotFile(keepDotFile);
        context.setExecuteDot(executeDot);
        context.setIgnoreDotFailure(ignoreDotFailure);
        return context;
    }

    protected String[] getArgs(DotContext context) {
        List<String> args = new ArrayList<String>();
        args.add("-T" + context.getType());
        args.add("-o" + context.getGraph().getAbsolutePath());
        args.add(context.getDotFile().getAbsolutePath());
        return args.toArray(new String[args.size()]);
    }

    protected Commandline getCommandLine(DotContext context) {
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

    protected int execute(Commandline commandLine, DotContext context) {
        try {
            StreamConsumer stdout = new DefaultConsumer();
            StreamConsumer stderr = new DefaultConsumer();
            int exitValue = CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
            if (exitValue != 0) {
                if (context.isIgnoreDotFailure()) {
                    throw new GraphException(getErrorMessage(context, exitValue));
                } else {
                    logger.info("Ignoring failure of the 'dot' binary");
                }
            }
            return exitValue;
        } catch (CommandLineException e) {
            throw new GraphException(e);
        }
    }

    protected String getErrorMessage(DotContext context, int exitValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("Execution of the '" + context.getExecutable() + "' command failed.");
        sb.append(" Exit value: '" + exitValue + "'");
        sb.append(" Is it installed? See: http://www.graphviz.org");
        return sb.toString();
    }

    public void execute(DotContext context) {
        if (context.isExecuteDot()) {
            Commandline commandline = getCommandLine(context);
            execute(commandline, context);
            logger.info(context.getGraph().getPath());
        } else {
            logger.info("Skip executing 'dot'");
        }
        if (!context.isKeepDotFile()) {
            context.getDotFile().delete();
        } else {
            logger.info(context.getDotFile().getPath());
        }
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
