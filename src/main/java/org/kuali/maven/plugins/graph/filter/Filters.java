package org.kuali.maven.plugins.graph.filter;

import java.util.List;

import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.kuali.maven.plugins.graph.tree.Helper;

public class Filters {
    public <T> Filter<T> getIncludeExcludePatternFilter(String includes, String excludes, TokenCollector<T> collector) {
        Filter<T> include = getIncludePatternFilter(includes, collector);
        Filter<T> exclude = getExcludePatternFilter(excludes, collector);
        return new IncludeExcludeFilter<T>(include, exclude);
    }

    public <T> Filter<T> getIncludePatternFilter(String includes, TokenCollector<T> collector) {
        Filter<T> matchEverything = matchEverything();
        return getPatternFilter(includes, matchEverything, collector);
    }

    public <T> Filter<T> getExcludePatternFilter(String excludes, TokenCollector<T> collector) {
        Filter<T> matchNothing = matchNothing();
        return getPatternFilter(excludes, matchNothing, collector);
    }

    public <T> Filter<T> getPatternFilter(String csv, Filter<T> defaultFilter, TokenCollector<T> collector) {
        List<String> patterns = Helper.splitAndTrimCSVToList(csv);
        if (Helper.isEmpty(patterns)) {
            return defaultFilter;
        } else {
            return new PatternsFilter<T>(patterns, collector);
        }
    }

    public static final <T> Filter<T> matchNothing() {
        return new MatchNothingFilter<T>();
    }

    public static final <T> Filter<T> matchEverything() {
        return new MatchEverythingFilter<T>();
    }
}
