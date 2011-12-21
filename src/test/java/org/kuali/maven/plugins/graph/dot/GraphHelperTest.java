package org.kuali.maven.plugins.graph.dot;

import org.junit.Test;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphHelperTest {
    private static final Logger logger = LoggerFactory.getLogger(GraphHelperTest.class);

    @Test
    public void test() {
        String title = "Hello World";
        GraphHelper gh = new GraphHelper();
        Table table = gh.getLegendTable(title, null);
        String s = gh.toHtml(table);
        logger.info(s);

    }

}
