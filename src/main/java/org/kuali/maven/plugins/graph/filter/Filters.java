/**
 * Copyright 2010-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.plugins.graph.filter;

import java.util.List;

import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.kuali.maven.plugins.graph.util.Helper;

/**
 * <p>
 * Utility class for working with <code>Filter's</code>.
 * </p>
 */
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
