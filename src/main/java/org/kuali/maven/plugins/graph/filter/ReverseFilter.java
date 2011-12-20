package org.kuali.maven.plugins.graph.filter;

/**
 *
 */
public class ReverseFilter<T> implements Filter<T> {
    Filter<T> filter;

    public ReverseFilter() {
        this(null);
    }

    public ReverseFilter(Filter<T> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public boolean isMatch(T element) {
        return !filter.isMatch(element);
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }

}
