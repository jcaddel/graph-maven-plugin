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
