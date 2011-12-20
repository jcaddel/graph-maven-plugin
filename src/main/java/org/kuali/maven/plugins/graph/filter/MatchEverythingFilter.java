package org.kuali.maven.plugins.graph.filter;

/**
 * Always returns true
 */
public class MatchEverythingFilter<T> implements Filter<T> {

    @Override
    public boolean isMatch(T element) {
        return true;
    }

}
