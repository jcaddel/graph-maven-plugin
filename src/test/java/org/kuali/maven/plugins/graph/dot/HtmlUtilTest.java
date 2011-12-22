package org.kuali.maven.plugins.graph.dot;

import org.junit.Test;
import org.kuali.maven.plugins.graph.dot.html.Font;
import org.kuali.maven.plugins.graph.dot.html.HtmlUtils;
import org.kuali.maven.plugins.graph.dot.html.Label;
import org.kuali.maven.plugins.graph.dot.html.Table;
import org.kuali.maven.plugins.graph.dot.html.TableCell;
import org.kuali.maven.plugins.graph.dot.html.TableRow;
import org.kuali.maven.plugins.graph.dot.html.Text;
import org.kuali.maven.plugins.graph.dot.html.TextItem;

public class HtmlUtilTest {

    HtmlUtils htmlUtil = new HtmlUtils();

    @Test
    public void test1() {
        Font font = new Font();
        TextItem textItem = new TextItem("hello world");
        Text text = new Text(textItem);
        Label label = new Label(text);
        TableCell cell = new TableCell();
        cell.setLabel(label);
        TableRow row = new TableRow(cell);
        Table table = new Table(row);
        table.setBorder(1);
        String html = htmlUtil.toHtml(table);
        System.out.println(html);
        // System.out.println(label.toHtml());

        TableRow row1 = new TableRow(new TableCell(new Label(new Text(new TextItem("Dependency Graph")))));
        TableRow row2 = new TableRow(new TableCell(new Label(new Text(new TextItem("show ::conflict")))));
        // Table table = new Table(row1, row2);
        // String s = htmlUtil.toHtml(new Label(table));
        // System.out.println(s);
    }

    @Test
    public void test2() {
        Font font = new Font(new Text(new TextItem("whatever")), "cornflowerblue", "8");
        TableCell cell = new TableCell(new Label(new Text(new TextItem(font))));
        String s = htmlUtil.toHtml(cell);
        // System.out.println(s);
    }

}
