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

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.kuali.maven.plugins.graph.dot.GraphException;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.pojo.Style;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class StyledTreeHelperTest {

    @Test
    public void test() {
        try {
            TreeHelper sth = new TreeHelper();
            Style defaultStyle = sth.getStyle(Scope.COMPILE, false, State.INCLUDED);
            System.out.println(describe(defaultStyle));
            Style optional = sth.getStyle(Scope.COMPILE, true, State.INCLUDED);
            System.out.println(describe(optional));
            for (State state : State.values()) {
                for (Scope scope : Scope.values()) {
                    Style theStyle = sth.getStyle(scope, false, state);
                    System.out.println(state.getValue() + " " + scope.getValue() + " " + describe(theStyle));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Map<?, ?> describe(Object bean) {
        try {
            Map<?, ?> map = BeanUtils.describe(bean);
            map.remove("class");
            return map;
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }

}
