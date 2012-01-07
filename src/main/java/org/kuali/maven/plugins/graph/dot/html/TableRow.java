/**
 * Copyright 2011-2012 The Kuali Foundation
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableRow implements HtmlTag {
    HtmlUtils htmlUtils = new HtmlUtils();

    public TableRow() {
        super();
    }

    public TableRow(TableCell cell) {
        this(Collections.singletonList(cell));
    }

    public TableRow(TableCell... cells) {
        this(Arrays.asList(cells));
    }

    public TableRow(List<TableCell> cells) {
        super();
        this.cells = cells;
    }

    @Override
    public String getName() {
        return "tr";
    }

    @Override
    public String getContent() {
        return htmlUtils.toHtml(cells);
    }

    List<TableCell> cells;

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

}
