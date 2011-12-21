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

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.pojo.Hider;
import org.kuali.maven.plugins.graph.tree.Helper;

public class GraphNodeGenerator {
    public static final String DEFAULT_TYPE = "jar";

    protected void add(List<String> list, String s, boolean skip) {
        if (skip || Helper.isBlank(s)) {
            return;
        } else {
            list.add(s);
        }
    }

    public String getLabel(Artifact a) {
        return getLabel(a, new Hider());
    }

    public String getLabel(Artifact a, Hider hider) {

        boolean hideType = hider.isHideType() || DEFAULT_TYPE.equalsIgnoreCase(a.getType());

        List<String> labelTokens = new ArrayList<String>();
        add(labelTokens, a.getGroupId(), hider.isHideGroupId());
        add(labelTokens, a.getArtifactId(), hider.isHideArtifactId());
        add(labelTokens, a.getType(), hideType);
        add(labelTokens, a.getClassifier(), hider.isHideClassifier());
        add(labelTokens, a.getVersion(), hider.isHideVersion());
        return getLabel(labelTokens);
    }

    public static String getLabel(List<String> tokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            if (i != 0) {
                sb.append("\\n");
            }
            sb.append(tokens.get(i));
        }
        return sb.toString();
    }

}
