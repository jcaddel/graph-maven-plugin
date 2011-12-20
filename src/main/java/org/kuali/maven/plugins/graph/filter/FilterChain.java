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

import static org.kuali.maven.plugins.graph.filter.MatchCondition.ALL;
import static org.kuali.maven.plugins.graph.filter.MatchCondition.ANY;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * By default, return true only if there is a match for every filter in the list provided. Set <code>condition</code> to
 * <code>ANY</code>, to return true for a match with any filter.
 * </p>
 * <p>
 * By default, if no filters are provided, return true. Set <code>defaultReturnValue</code> to <code>false</code> for
 * the opposite behavior
 * </p>
 */
public class FilterChain<T> implements Filter<T> {
    private static final Logger logger = LoggerFactory.getLogger(FilterChain.class);
    public static final MatchCondition DEFAULT_MATCH_CONDITION = MatchCondition.ALL;
    public static final boolean DEFAULT_RETURN_VALUE = true;

    boolean defaultReturnValue;
    MatchCondition condition;
    List<? extends Filter<T>> filters;

    public FilterChain() {
        this(new ArrayList<Filter<T>>());
    }

    public FilterChain(List<? extends Filter<T>> filters) {
        this(filters, DEFAULT_MATCH_CONDITION);
    }

    public FilterChain(List<? extends Filter<T>> filters, MatchCondition condition) {
        this(filters, condition, DEFAULT_RETURN_VALUE);
    }

    public FilterChain(List<? extends Filter<T>> filters, MatchCondition condition, boolean defaultReturnValue) {
        super();
        this.filters = filters;
        this.condition = condition;
        this.defaultReturnValue = defaultReturnValue;
    }

    @Override
    public boolean isMatch(T element) {
        if (filters == null || filters.size() == 0) {
            return defaultReturnValue;
        }
        for (Filter<T> filter : filters) {
            boolean match = filter.isMatch(element);
            if (match && condition == ANY) {
                return true;
            } else if (!match && condition == ALL) {
                return false;
            }
        }
        switch (condition) {
        case ALL:
            logger.debug("all matched");
            return true;
        case ANY:
            logger.debug("none matched");
            return false;
        default:
            throw new FilterException("Unknown condition " + condition);
        }
    }

    public MatchCondition getCondition() {
        return condition;
    }

    public void setCondition(MatchCondition condition) {
        this.condition = condition;
    }

    public List<? extends Filter<T>> getFilters() {
        return filters;
    }

    public void setFilters(List<? extends Filter<T>> filters) {
        this.filters = filters;
    }

}
