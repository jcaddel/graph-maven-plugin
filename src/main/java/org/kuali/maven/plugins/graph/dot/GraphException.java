package org.kuali.maven.plugins.graph.dot;

public class GraphException extends RuntimeException {

    private static final long serialVersionUID = 1924410233882190818L;

    public GraphException() {
    }

    public GraphException(String message) {
        super(message);
    }

    public GraphException(Throwable exception) {
        super(exception);
    }

    public GraphException(String message, Throwable exception) {
        super(message, exception);
    }

}
