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
