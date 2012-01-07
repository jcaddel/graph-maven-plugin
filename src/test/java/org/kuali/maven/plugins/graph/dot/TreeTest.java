/**
 * Copyright 2011-2012 The Kuali Foundation
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
