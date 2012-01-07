/**
 * Copyright 2010-2012 The Kuali Foundation
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

public class Font implements HtmlTag {
    String color;
    String face;
    Integer pointSize;
    String content;

    public Font() {
        this(null, null, null, null);
    }

    public Font(String content) {
        this(content, null, null, null);
    }

    public Font(String content, String color) {
        this(content, color, null, null);
    }

    public Font(String color, Integer pointSize) {
        this(null, color, null, pointSize);
    }

    public Font(String content, String color, Integer pointSize) {
        this(content, color, null, pointSize);
    }

    public Font(String content, String color, String face, Integer pointSize) {
        super();
        this.content = content;
        this.color = color;
        this.face = face;
        this.pointSize = pointSize;
    }

    @Override
    public String getName() {
        return "font";
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getPointSize() {
        return pointSize;
    }

    public void setPointSize(Integer pointSize) {
        this.pointSize = pointSize;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
