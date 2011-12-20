package org.kuali.maven.plugins.graph.sanitize;


public interface Sanitizer<T> {

    void sanitize(T object);

}
