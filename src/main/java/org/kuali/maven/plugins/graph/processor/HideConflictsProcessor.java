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

import java.util.List;

import org.kuali.maven.plugins.graph.dot.EdgeGenerator;
import org.kuali.maven.plugins.graph.pojo.Conflicts;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class HideConflictsProcessor implements Processor {
    TreeHelper helper = new TreeHelper();
    EdgeGenerator generator = new EdgeGenerator();
    GraphDescriptor descriptor;

    public HideConflictsProcessor() {
        this(null);
    }

    public HideConflictsProcessor(GraphDescriptor descriptor) {
        super();
        this.descriptor = descriptor;
    }

    @Override
    public void process(Node<MavenContext> root) {
        switch (descriptor.getLayout()) {
        case LINKED:
            handleLinkedConflicts(root);
            return;
        case FLAT:
            handleFlatConflicts(root);
            return;
        default:
            throw new IllegalStateException("Unknown layout " + descriptor.getLayout());
        }
    }

    protected void handleLinkedConflicts(Node<MavenContext> root) {
        List<Node<MavenContext>> conflicts = helper.getNodeList(root, State.CONFLICT);
        for (Node<MavenContext> conflict : conflicts) {
            String replacementId = TreeHelper.getArtifactId(conflict.getObject().getReplacement());
            Node<MavenContext> replacement = TreeHelper.findRequiredIncludedNode(root, replacementId);
            Conflicts conflictMode = descriptor.getConflicts();
            State state = State.CONFLICT;
            if (conflictMode == Conflicts.IGNORE) {
                state = State.INCLUDED;
            }
            Edge edge = generator.getParentChildEdge(conflict, replacement, state);
            generator.addEdge(conflict.getParent(), edge);
            helper.hide(conflict);
        }
    }

    protected void handleFlatConflicts(Node<MavenContext> root) {
        List<Node<MavenContext>> conflicts = helper.getNodeList(root, State.CONFLICT);
        for (Node<MavenContext> conflict : conflicts) {
            helper.hide(conflict);
        }
    }

    public GraphDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(GraphDescriptor descriptor) {
        this.descriptor = descriptor;
    }

}
