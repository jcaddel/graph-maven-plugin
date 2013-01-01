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
package org.kuali.maven.plugins.graph.validate;

import org.kuali.maven.plugins.graph.tree.Node;

/**
 * <p>
 * Perform validation on a tree whose nodes contain a specific type of object.
 * </p>
 *
 * @param <T>
 */
public interface NodeValidator<T> extends Validator<Node<T>> {

}
