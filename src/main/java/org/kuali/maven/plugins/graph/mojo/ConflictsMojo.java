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
package org.kuali.maven.plugins.graph.mojo;


/**
 * <p>
 * This mojo displays any dependencies where conflict resolution has taken place.
 * </p>
 *
 * <p>
 * Maven supports the resolution of artifact versions by way of nearest-wins. That is, for any set of dependencies
 * sharing the same [groupId]:[artifactId]:[type]:[classifier], the one declared nearest to the current project in the
 * dependency tree is selected for use.
 * </p>
 *
 * @goal conflicts
 * @requiresDependencyResolution compile|test|runtime
 */
public class ConflictsMojo {

}