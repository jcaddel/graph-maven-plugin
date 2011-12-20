package org.kuali.maven.plugins.graph.filter;

/**
 * Return true if the element matches the criteria for the filter, false otherwise
 */
public interface Filter<T> {

    boolean isMatch(T element);

}
