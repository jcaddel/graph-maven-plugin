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
package org.kuali.maven.plugins.graph.dot.html.copy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;

public class HtmlUtils {

    protected boolean isSkip(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Collection) {
            return true;
        }
        if (value instanceof HtmlTag) {
            return true;
        }
        return false;
    }

    public <T extends HtmlTag> T cloneAttributes(T element) {
        Map<String, ?> attributes = getAttributes(element);
        T newElement = newInstance(element);
        for (String property : attributes.keySet()) {
            Object value = attributes.get(property);
            setProperty(newElement, property, value);
        }
        return newElement;
    }

    @SuppressWarnings("unchecked")
    protected <T> T newInstance(T object) {
        try {
            return (T) object.getClass().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Map<String, ?> getAttributes(HtmlTag element) {
        Map<String, ?> description = describe(element);
        List<String> keys = new ArrayList<String>(description.keySet());
        for (String key : keys) {
            Object value = description.get(key);
            if (isSkip(value)) {
                description.remove(key);
            }
        }
        description.remove("class");
        description.remove("name");
        description.remove("content");
        return description;
    }

    public String toHtml(List<? extends HtmlTag> tags) {
        if (tags == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (HtmlTag tag : tags) {
            sb.append(toHtml(tag));
        }
        return sb.toString();
    }

    public String toHtml(HtmlTag tag) {
        String name = tag.getName();
        String content = tag.getContent();
        Map<String, ?> attributeMap = getAttributes(tag);
        String attributes = toHtml(attributeMap);

        if (content == null || content.trim().length() == 0) {
            return "<" + name + attributes + "/>";
        } else {
            return "<" + name + attributes + ">" + content + "</" + name + ">";
        }
    }

    public String toHtml(Map<String, ?> attributes) {
        if (attributes.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : attributes.keySet()) {
            String value = attributes.get(key).toString();
            String name = getTranslatedAttributeName(key);
            sb.append(name + "=" + quote(value) + " ");
        }
        sb.replace(sb.lastIndexOf(" "), sb.length(), "");
        return " " + sb.toString();
    }

    protected String getTranslatedAttributeName(String name) {
        if (name.equals("pointSize")) {
            return "point-size";
        } else if (name.equals("rowCount")) {
            return "rows";
        } else {
            return name;
        }
    }

    public String quote(String s) {
        return '"' + s + '"';
    }

    @SuppressWarnings("unchecked")
    public Map<String, ?> describe(HtmlTag element) {
        try {
            return new TreeMap<String, Object>(PropertyUtils.describe(element));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void setProperty(HtmlTag element, String property, Object value) {
        try {
            PropertyUtils.setProperty(element, property, value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
