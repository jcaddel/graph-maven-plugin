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
package org.kuali.maven.plugins.graph.sanitize;

import java.util.List;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Included;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MavenContextSanitizer implements NodeSanitizer<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(MavenContextSanitizer.class);

    TreeHelper helper = new TreeHelper();

    Included included;
    State state;

    public MavenContextSanitizer() {
        this(null, null);
    }

    public MavenContextSanitizer(Included included, State state) {
        super();
        this.included = included;
        this.state = state;
    }

    protected abstract void sanitize(MavenContext context, Included included);

    @Override
    public void sanitize(Node<MavenContext> node) {
        List<MavenContext> contexts = helper.getList(node, state);
        for (MavenContext context : contexts) {
            State elementState = context.getState();
            if (state.equals(elementState)) {
                sanitize(context, included);
            }
        }
    }

    protected void warnAndSwitch(State switchTo, String artifactId, MavenContext context, String warning) {
        logger.warn(getSwitchMessage(artifactId, switchTo));
        logger.warn(warning);
        context.setState(switchTo);
    }

    protected String getSwitchMessage(String artifactId, State switched) {
        return getState() + "->" + switched + " " + artifactId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Included getIncluded() {
        return included;
    }

    public void setIncluded(Included included) {
        this.included = included;
    }
}
