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
package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.validate.ConflictDependencyNodeValidator;
import org.kuali.maven.plugins.graph.validate.DuplicateDependencyNodeValidator;
import org.kuali.maven.plugins.graph.validate.IncludedDependencyNodeValidator;
import org.kuali.maven.plugins.graph.validate.NodeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Validate the dependency tree
 * </p>
 */
public class ValidatingProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ValidatingProcessor.class);

    @Override
    public void process(Node<MavenContext> node) {
        List<NodeValidator<MavenContext>> validators = getValidators(node);
        for (NodeValidator<MavenContext> validator : validators) {
            validator.validate(node);
        }
        logger.debug("Validation complete");
    }

    protected List<NodeValidator<MavenContext>> getValidators(Node<MavenContext> node) {
        List<NodeValidator<MavenContext>> validators = new ArrayList<NodeValidator<MavenContext>>();
        validators.add(new IncludedDependencyNodeValidator());
        validators.add(new DuplicateDependencyNodeValidator());
        validators.add(new ConflictDependencyNodeValidator());
        return validators;
    }

}
