package org.kuali.maven.plugins.graph.dot.html;

public class Label<T> {
    T element;

    public Label() {
        this(null);
    }

    public Label(T element) {
        super();
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
}
