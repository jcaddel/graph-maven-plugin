package org.kuali.maven.plugins.graph.filter;

/**
 * Always returns false
 */
public class MatchNothingFilter<T> implements Filter<T> {

    @Override
    public boolean isMatch(T element) {
        return false;
    }

}
