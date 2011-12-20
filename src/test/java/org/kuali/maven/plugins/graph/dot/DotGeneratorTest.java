package org.kuali.maven.plugins.graph.dot;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kuali.maven.plugins.graph.dot.StringGenerator;
import org.kuali.maven.plugins.graph.pojo.Graph;

public class DotGeneratorTest {
    DotGenerator dg = new DotGenerator();
    StringGenerator sg = new StringGenerator();

    @Test
    public void testHelloWorld() {
        try {
            Graph g = dg.getHelloWorld();
            String s = sg.getString(g);
            File file = new File("./target/test-classes/hellworld.dot");
            OutputStream out = FileUtils.openOutputStream(file);
            IOUtils.write(s.getBytes(), out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
