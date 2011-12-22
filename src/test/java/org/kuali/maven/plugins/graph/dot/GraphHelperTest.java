package org.kuali.maven.plugins.graph.dot;

import org.junit.Test;
import org.kuali.maven.plugins.graph.dot.html.Table;

public class GraphHelperTest {

    @Test
    public void test() {
        String title = "Hello World";
        GraphHelper gh = new GraphHelper();
        Table table = gh.getLegendTable(title, null);
        String s = gh.toHtml(table);
        System.out.println(s);
    }

}
