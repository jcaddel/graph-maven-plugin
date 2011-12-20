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
package org.kuali.maven.plugins.graph.filter;

import java.util.List;

import org.kuali.maven.plugins.graph.tree.Node;

/**
 *
 */

public class NodeFilterChain<T> extends FilterChain<Node<T>> implements NodeFilter<T> {

    public NodeFilterChain(List<? extends Filter<Node<T>>> filters, MatchCondition condition,
            boolean defaultReturnValue) {
        super(filters, condition, defaultReturnValue);
    }

    public NodeFilterChain(List<? extends Filter<Node<T>>> filters, MatchCondition condition) {
        super(filters, condition);
    }

    public NodeFilterChain() {
        super();
    }

    public NodeFilterChain(List<NodeFilter<T>> filters) {
        super(filters);
    }

}
