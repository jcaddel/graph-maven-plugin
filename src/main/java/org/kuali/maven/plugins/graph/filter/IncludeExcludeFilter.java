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
