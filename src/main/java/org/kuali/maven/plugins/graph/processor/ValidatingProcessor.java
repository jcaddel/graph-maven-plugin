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
