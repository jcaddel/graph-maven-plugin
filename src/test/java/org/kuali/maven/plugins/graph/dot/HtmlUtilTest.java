package org.kuali.maven.plugins.graph.dot;

import org.junit.Test;
import org.kuali.maven.plugins.graph.dot.html.HtmlUtil;
import org.kuali.maven.plugins.graph.dot.html.Label;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.kuali.maven.plugins.graph.dot.html.TableCell;
import org.kuali.maven.plugins.graph.dot.html.TableRow;
import org.kuali.maven.plugins.graph.dot.html.Text;
import org.kuali.maven.plugins.graph.dot.html.TextItem;

public class HtmlUtilTest {

    HtmlUtil htmlUtil = new HtmlUtil();

    @Test
    public void test() {
        System.out.println(htmlUtil.toHtml(new Label(new Text(new TextItem("hello world")))));

        TableRow row1 = new TableRow(new TableCell(new Label(new Text(new TextItem("Dependency Graph")))));
        TableRow row2 = new TableRow(new TableCell(new Label(new Text(new TextItem("show ::conflict")))));
        Table table = new Table(row1, row2);
        String s = htmlUtil.toHtml(new Label(table));
        System.out.println(s);
    }

}
