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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.kuali.maven.plugins.graph.dot.html.enums.TableCellAlign;
import org.kuali.maven.plugins.graph.tree.Helper;

public class HtmlUtil {

    public List<TableCell> getTableCells(List<String> contents, TableCellAlign align, Font font) {
        List<TableCell> cells = new ArrayList<TableCell>();
        for (String content : contents) {
            TableCell cell = getTableCell(content, align, font);
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

    public String toHtml(TableCell cell) {
        StringBuilder sb = new StringBuilder();
        sb.append("<TD" + getAttributesHtml(cell) + ">");
        if (cell.getLabel() != null) {
            sb.append(toHtml(cell.getLabel()));
        } else if (cell.getImg() != null) {
            sb.append(toHtml(cell.getImg()));
        }
        sb.append("</TD>");
        return sb.toString();
    }

    public String toHtml(Label label) {
        StringBuilder sb = new StringBuilder();
        if (label.getText() != null) {
            sb.append(toHtml(label.getText()));
        } else if (label.getTable() != null) {
            sb.append(toHtml(label.getTable()));
        }
        return sb.toString();
    }

    public String toHtml(Text text) {
        StringBuilder sb = new StringBuilder();
        if (text.getText() != null) {
            sb.append(toHtml(text.getText()));
        } else if (text.getTextItem() != null) {
            sb.append(toHtml(text.getTextItem()));
        }
        return sb.toString();
    }

    public String toHtml(TextItem textItem) {
        StringBuilder sb = new StringBuilder();
        if (textItem.getBr() != null) {
            sb.append(toHtml(textItem.getBr()));
        } else if (textItem.getString() != null) {
            sb.append(textItem.getString());
        } else if (textItem.getFont() != null) {
            sb.append(toHtml(textItem.getFont()));
        }
        return sb.toString();
    }

    public void copyAttributes(TableCell dest, TableCell orig) {
        Map<String, Object> attributes = getAttributes(orig);
        for (String key : attributes.keySet()) {
            Object value = attributes.get(key);
            copyProperty(dest, key, value);
        }
    }

    public void copyAttributes(Font dest, Font orig) {
        Map<String, ?> attributes = getAttributes(orig);
        for (String key : attributes.keySet()) {
            Object value = attributes.get(key);
            copyProperty(dest, key, value);
        }
    }

    protected Map<String, Object> getAttributes(Object object, String... excludes) {
        Map<String, Object> attributes = describe(object, excludes);
        attributes.remove("class");
        return attributes;
    }

    protected Map<String, Object> getAttributes(Font font) {
        return getAttributes(font, "text");
    }

    protected Map<String, Object> getAttributes(TableCell cell) {
        return describe(cell, "img", "label");
    }

    protected String getAttributesHtml(TableCell cell) {
        Map<String, Object> attributes = getAttributes(cell);
        return toHtml(attributes);
    }

    protected String getAttributesHtml(Object object) {
        Map<String, Object> attributes = getAttributes(object);
        return toHtml(attributes);
    }

    protected String getAttributesHtml(Font font) {
        Map<String, Object> attributes = getAttributes(font);
        Object pointSize = attributes.get("pointSize");
        if (pointSize != null) {
            attributes.put("POINT-SIZE", pointSize);
            attributes.remove("pointSize");
        }
        return toHtml(attributes);
    }

    public String toHtml(Font font) {
        StringBuilder sb = new StringBuilder();
        sb.append("<FONT" + getAttributesHtml(font) + ">");
        if (font.getText() != null) {
            sb.append(toHtml(font.getText()));
        }
        sb.append("</FONT>");
        return sb.toString();
    }

    public String toHtml(Br br) {
        return "<BR" + getAttributesHtml(br) + " />";
    }

    public String toHtml(Img img) {
        return "<IMG" + getAttributesHtml(img) + " />";
    }

    public String getHtml(List<TableCell> cells) {
        if (Helper.isEmpty(cells)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (TableCell cell : cells) {
            sb.append(toHtml(cell));
        }
        return sb.toString();
    }

    public String toHtml(List<TableRow> rows) {
        if (Helper.isEmpty(rows)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (TableRow row : rows) {
            sb.append(toHtml(row));
        }
        return sb.toString();
    }

    public String toHtml(TableRow row) {
        if (Helper.isEmpty(row.getCells())) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<TR>");
        sb.append(getHtml(row.getCells()));
        sb.append("</TR>");
        return sb.toString();
    }

    public String toHtml(Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("<TABLE" + getAttributesHtml(table, "rows") + ">");
        sb.append(toHtml(table.getTrs()));
        sb.append("</TABLE>");
        return sb.toString();
    }

    public String toHtml(Map<String, Object> attributes) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String key : attributes.keySet()) {
            Object value = attributes.get(key);
            if (value == null) {
                continue;
            }
            if (count++ != 0) {
                sb.append(" ");
            }
            sb.append(key + "=" + quote(value.toString()));
        }
        if (count > 0) {
            return " " + sb.toString();
        } else {
            return sb.toString();
        }
    }

    protected String getAttributesHtml(Object object, String... excludes) {
        Map<String, Object> description = describe(object, excludes);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String key : description.keySet()) {
            Object value = description.get(key);
            if (value == null) {
                continue;
            }
            if (count++ != 0) {
                sb.append(" ");
            }
            sb.append(key.toUpperCase() + "=" + quote(value.toString()));
        }
        if (count > 0) {
            return " " + sb.toString();
        } else {
            return sb.toString();
        }
    }

    public String quote(String s) {
        return '"' + s + '"';
    }

    protected Map<String, Object> describe(Object bean, String... excludes) {
        Map<String, Object> description = describe(bean);
        remove(description, excludes);
        return description;
    }

    protected void remove(Map<String, Object> description, String... keys) {
        if (keys != null) {
            for (String key : keys) {
                description.remove(key);
            }
        }
    }

    protected void copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.copyProperty(bean, name, value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> describe(Object bean) {
        try {
            return new TreeMap<String, Object>(BeanUtils.describe(bean));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
