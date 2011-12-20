package org.kuali.maven.plugins.graph.dot;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.Test;

public class TreeTest {

    @Test
    public void testTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode child = new DefaultMutableTreeNode();
        root.add(child);

        Enumeration<?> e = root.children();
        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) element;
            Object userObject = dmtn.getUserObject();
            System.out.println(element.getClass());
            System.out.println(dmtn);
            System.out.println(userObject);
        }
    }
}
