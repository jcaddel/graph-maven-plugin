package org.kuali.maven.plugins.graph.mojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.kuali.maven.plugins.graph.pojo.Folder;

public class MojoHelperTest {

    MojoHelper helper = new MojoHelper();

    @Test
    public void test1() {
        Folder p = new Folder("examples");
        Folder c1 = new Folder("logging");
        Folder c2 = new Folder("closure");
        c1.setParent(p);
        c2.setParent(p);

        Folder gc = new Folder("klog");
        gc.setParent(c1);

        c1.setFolders(Collections.singletonList(gc));

        List<Folder> children = new ArrayList<Folder>();
        children.add(c1);
        children.add(c2);
        p.setFolders(children);

        System.out.println(helper.getPath(gc));
    }
}
