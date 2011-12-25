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
package org.kuali.maven.plugins.graph.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections15.list.SetUniqueList;
import org.apache.commons.collections15.map.ListOrderedMap;

/**
 * <p>
 * General purpose utility methods.
 * </p>
 */
public class Helper {
    public static final String COMMA = ",";
    public static final String EMPTY_STRING = "";

    public static final void copyProperties(Object dest, Object orig) {
        try {
            PropertyUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static final <T> Object getProperty(T bean, String property) {
        try {
            return PropertyUtils.getProperty(bean, property);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static final <T> String toCSV(List<T> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(COMMA);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public static final <T> ListOrderedMap<String, String> describe(T element, String... properties) {
        return describe(element, Arrays.asList(properties));
    }

    public static final <T> ListOrderedMap<String, String> describe(T element, List<String> properties) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = BeanUtils.describe(element);
            ListOrderedMap<String, String> orderedMap = new ListOrderedMap<String, String>();
            for (String property : properties) {
                Object value = map.get(property);
                if (value == null) {
                    continue;
                }
                orderedMap.put(property, value.toString());
            }
            return orderedMap;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static final <T> SetUniqueList<T> decorate() {
        return SetUniqueList.decorate(new ArrayList<T>());
    }

    public static final String[] toArray(List<String> strings) {
        return strings.toArray(new String[strings.size()]);
    }

    public static final String toEmpty(String s) {
        if (isBlank(s)) {
            return EMPTY_STRING;
        } else {
            return s;
        }
    }

    /**
     * Null safe method for adding all the elements in "from" to "to"
     */
    public static final <T> void addAll(Collection<T> to, Collection<T> from) {
        if (!isEmpty(from)) {
            to.addAll(from);
        }
    }

    /**
     * True if the collection is null or has no elements, false otherwise
     */
    public static final boolean isEmpty(Collection<?> c) {
        return c == null || c.size() == 0;
    }

    /**
     * True if the string is null or whitespace only, false otherwise
     */
    public static final boolean isBlank(String s) {
        return s == null || s.trim().equals(EMPTY_STRING);
    }

    /**
     * Return a List (never null) created from the csv string. Each csv token is trimmed before being added to the List.
     * If the csv string is null or contains only whitespace an empty list is returned.
     */
    public static final List<String> splitAndTrimCSVToList(String csv) {
        if (isBlank(csv)) {
            return Collections.emptyList();
        }
        String[] tokens = csv.split(COMMA);
        List<String> list = new ArrayList<String>();
        for (String token : tokens) {
            list.add(token.trim());
        }
        return list;
    }

}
