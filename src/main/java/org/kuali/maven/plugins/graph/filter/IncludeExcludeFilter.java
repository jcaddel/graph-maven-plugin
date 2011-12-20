package org.kuali.maven.plugins.graph.filter;

/**
 * <p>
 * Returns true if an element matches the include filter but not the exclude filter
 * </p>
 *
 * <p>
 * By default, everything is included and nothing is excluded.
 * </p>
 *
 * <p>
 * The default include filter matches everything, and the default exclude filter matches nothing
 * </p>
 *
 */
public class IncludeExcludeFilter<T> implements Filter<T> {
    Filter<T> include;
    Filter<T> exclude;

    public IncludeExcludeFilter() {
        this(Filters.<T> matchEverything(), Filters.<T> matchNothing());
    }

    public IncludeExcludeFilter(Filter<T> include, Filter<T> exclude) {
        super();
        this.include = include;
        this.exclude = exclude;
    }

    @Override
    public boolean isMatch(T element) {
        boolean included = include.isMatch(element);
        boolean excluded = exclude.isMatch(element);
        return included && !excluded;
    }

    public Filter<T> getInclude() {
        return include;
    }

    public void setInclude(Filter<T> include) {
        this.include = include;
    }

    public Filter<T> getExclude() {
        return exclude;
    }

    public void setExclude(Filter<T> exclude) {
        this.exclude = exclude;
    }

}
