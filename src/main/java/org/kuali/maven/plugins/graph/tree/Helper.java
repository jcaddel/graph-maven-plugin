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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * General purpose utility methods.
 * </p>
 */
public class Helper {
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    public static final String COMMA = ",";
    public static final String EMPTY_STRING = "";

    /**
     * Copy properties from "orig" to "dest" where the property from "orig" is not null, and the property from "dest" is
     * null
     */
    public static final <T> void copyPropertiesIfNull(T dest, T orig) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, ?> description = PropertyUtils.describe(orig);
            for (String name : description.keySet()) {
                copyPropertyIfNull(dest, name, orig);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * If the property from "orig" is not null and that property from
     * "dest is null, copy the property from "orig" to "dest"
     */
    public static final void copyPropertyIfNull(Object dest, String name, Object orig) {
        try {
            Object oldValue = PropertyUtils.getProperty(dest, name);
            if (oldValue != null) {
                return;
            }
            Object newValue = PropertyUtils.getProperty(orig, name);
            logger.trace("name={} value={}", name, newValue);
            PropertyUtils.setProperty(dest, name, newValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static final <T> T copyProperties(Class<T> c, Object orig) {
        try {
            T dest = c.newInstance();
            PropertyUtils.copyProperties(dest, orig);
            return dest;
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

    public static final String toEmpty(Object o) {
        if (o == null) {
            return EMPTY_STRING;
        } else {
            return toEmpty(o.toString());
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
