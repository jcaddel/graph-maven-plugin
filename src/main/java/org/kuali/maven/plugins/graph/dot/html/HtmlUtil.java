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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.tree.Helper;

public class HtmlUtil {

    public String toHtml(TableCell cell) {
        StringBuilder sb = new StringBuilder();
        sb.append("<TD" + getAttributes(cell, "label", "img") + ">");
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

    public String getFontAttributes(Font font) {
        StringBuilder sb = new StringBuilder();
        sb.append(getAttributes(font, "text", "pointSize"));
        if (font.getPointSize() == null) {
            return sb.toString();
        }
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append("POINT-SIZE=" + quote(font.getPointSize()));
        return sb.toString();
    }

    public String toHtml(Font font) {
        StringBuilder sb = new StringBuilder();
        sb.append("<FONT" + getFontAttributes(font) + ">");
        if (font.getText() != null) {
            sb.append(toHtml(font));
        }
        sb.append("</FONT>");
        return sb.toString();
    }

    public String toHtml(Br br) {
        return "<BR" + getAttributes(br) + " />";
    }

    public String toHtml(Img img) {
        return "<IMG" + getAttributes(img) + " />";
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
        sb.append("<TABLE" + getAttributes(table, "rows", "font") + ">");
        sb.append(toHtml(table.getRows()));
        sb.append("</TABLE>");
        return sb.toString();
    }

    protected <T> String getAttributes(T object, String... excludes) {
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

    protected <T> Map<String, Object> describe(T bean, String... excludes) {
        Map<String, Object> description = describe(bean);
        for (String exclude : excludes) {
            description.remove(exclude);
        }
        description.remove("class");
        return description;
    }

    @SuppressWarnings("unchecked")
    protected <T> Map<String, Object> describe(T bean) {
        try {
            return new TreeMap<String, Object>(BeanUtils.describe(bean));
        } catch (Exception e) {
            throw new GraphException(e);
        }
    }
}
