package org.kuali.maven.plugins.graph.collector;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

/**
 * <p>
 * Provide filter tokens for a Maven <code>DependencyNode</code>
 * </p>
 *
 * <pre>
 * [scope]:[optional|required]:[state]
 * </pre>
 *
 * Scopes: <code>compile,provided,runtime,test,system,import</code><br>
 * States: <code>included,conflict,cyclic,duplicate</code><br>
 *
 * <p>
 * <code>compile</code> matches all nodes with compile scope<br>
 * <code>*:optional</code> or <code>:optional</code> matches all nodes that are optional<br>
 * <code>::normal</code> matches all nodes that are normal<br>
 * <code>::conflict</code> matches all nodes that have been omitted due to conflicts<br>
 * </p>
 */
public class DependencyNodeTokenCollector implements TokenCollector<DependencyNode> {

    /**
     *
     */
    @Override
    public List<String> getTokens(DependencyNode node) {
        Scope scope = Scope.getScope(node.getArtifact().getScope());
        if (scope == null) {
            scope = Scope.DEFAULT_SCOPE;
        }
        State state = State.getState(node.getState());
        String optional = node.getArtifact().isOptional() ? TreeHelper.OPTIONAL : TreeHelper.REQUIRED;
        List<String> tokens = new ArrayList<String>();
        tokens.add(scope.getValue());
        tokens.add(optional);
        tokens.add(state.getValue());
        return tokens;
    }
}
