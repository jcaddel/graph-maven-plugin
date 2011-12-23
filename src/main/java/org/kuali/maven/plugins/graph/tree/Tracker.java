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
package org.kuali.maven.plugins.graph.tree;

import java.util.HashMap;

/**
 * <p>
 * Very simple utility class for tracking occurrences of a string.
 * </p>
 *
 * @author jeffcaddel
 */
public class Tracker extends HashMap<String, Integer> {

    private static final long serialVersionUID = -2649198262211508724L;

    public void increment(String s) {
        if (get(s) == null) {
            put(s, 1);
        } else {
            put(s, get(s) + 1);
        }
    }

}
