package org.kuali.maven.plugins.graph.filter;

public class FilterException extends RuntimeException {

    private static final long serialVersionUID = 7631598834023805362L;

    public FilterException() {
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(Throwable exception) {
        super(exception);
    }

    public FilterException(String message, Throwable exception) {
        super(message, exception);
    }

}
