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
package org.kuali.maven.plugins.graph.dot.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.kuali.maven.plugins.graph.dot.html.enums.TableCellAlign;

public class HtmlUtils {

    protected boolean isSkip(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Collection) {
            return true;
        }
        if (value instanceof HtmlElement) {
            return true;
        }
        return false;
    }

    public <T extends HtmlElement> T copy(T element) {
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

    public Map<String, ?> getAttributes(HtmlElement element) {
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
        description.remove("string");
        return description;
    }

    public String toHtml(List<? extends HtmlElement> elements) {
        StringBuilder sb = new StringBuilder();
        for (HtmlElement element : elements) {
            sb.append(toHtml(element));
        }
        return sb.toString();
    }

    public String toHtml(HtmlElement element) {
        String name = element.getName();
        String attributes = toHtml(getAttributes(element));
        String string = (String) describe(element).get("string");
        String nested = toHtml(element.getElements());

        if (string != null) {
            return string;
        } else if (name == null) {
            return nested.toString();
        } else if (nested.length() == 0) {
            return "<" + name + attributes + "/>";
        } else {
            return "<" + name + attributes + ">" + nested + "</" + name + ">";
        }
    }

    public List<TableCell> getTableCells(List<String> contents, TableCellAlign align, Font font) {
        List<TableCell> cells = new ArrayList<TableCell>();
        for (String content : contents) {
            TableCell cell = getTableCell(content, align, copy(font));
            cells.add(cell);
        }
        return cells;
    }

    public TableCell getTableCell(String content, TableCellAlign align, Font font) {
        Text text = new Text(new TextItem(content));
        font.setText(text);
        TableCell cell = new TableCell(new Label(new Text(new TextItem(font))));
        cell.setAlign(align);
        return cell;
    }

    public String toHtml(Map<String, ?> attributes) {
        if (attributes.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : attributes.keySet()) {
            String value = attributes.get(key).toString();
            sb.append(key + "=" + quote(value) + " ");
        }
        sb.replace(sb.lastIndexOf(" "), sb.length(), "");
        return " " + sb.toString();
    }

    public String quote(String s) {
        return '"' + s + '"';
    }

    @SuppressWarnings("unchecked")
    public Map<String, ?> describe(HtmlElement element) {
        try {
            return new TreeMap<String, Object>(PropertyUtils.describe(element));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void setProperty(HtmlElement element, String property, Object value) {
        try {
            PropertyUtils.setProperty(element, property, value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
