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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class Node<T> extends DefaultMutableTreeNode {

    private static final long serialVersionUID = -4552044477560333925L;

    public Node() {
        this(null);
    }

    public Node(T userObject) {
        this(userObject, true);
    }

    public Node(T userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    @Override
    public Node<T>[] getPath() {
        TreeNode[] treeNodes = super.getPath();
        @SuppressWarnings("unchecked")
        Node<T>[] typedNodes = (Node<T>[]) Array.newInstance(this.getClass(), treeNodes.length);
        for (int i = 0; i < typedNodes.length; i++) {
            @SuppressWarnings("unchecked")
            Node<T> pathNode = (Node<T>) treeNodes[i];
            typedNodes[i] = pathNode;
        }
        return typedNodes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Node<T> getRoot() {
        return (Node<T>) super.getRoot();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<Node<T>> children() {
        return super.children();
    }

    @SuppressWarnings("unchecked")
    public List<Node<T>> getBreadthFirstList() {
        return getList(breadthFirstEnumeration());
    }

    @SuppressWarnings("unchecked")
    public List<Node<T>> getDepthFirstList() {
        return getList(depthFirstEnumeration());
    }

    protected <E> List<E> getList(Enumeration<E> e) {
        List<E> list = new ArrayList<E>();
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        return list;
    }

    public List<Node<T>> getChildren() {
        return getList(children());
    }

    @SuppressWarnings("unchecked")
    public T getObject() {
        return (T) super.getUserObject();
    }

    public void setObject(T object) {
        super.setUserObject(object);
    }

    /**
     * @deprecated Use getObject() instead
     */
    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public T getUserObject() {
        return (T) super.getUserObject();
    }

    /**
     * @deprecated Use setObject(T) instead
     */
    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public void setUserObject(Object userObject) {
        T object = (T) userObject;
        super.setUserObject(object);
    }

    public void add(Node<T> newChild) {
        super.add(newChild);
    }

    /**
     * @deprecated Use add(TypedNode<T> newChild) instead
     */
    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public void add(MutableTreeNode newChild) {
        Node<T> typedChild = (Node<T>) newChild;
        add(typedChild);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Node<T> getParent() {
        return (Node<T>) super.getParent();
    }

}
