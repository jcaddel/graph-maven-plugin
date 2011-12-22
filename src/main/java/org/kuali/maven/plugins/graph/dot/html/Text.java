package org.kuali.maven.plugins.graph.dot.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

public class Text extends NestedElement {

    TextItem textItem;
    Text text;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getElementNames() {
        List<String> names = new ArrayList<String>();
        names.add("text");
        names.add("textItem");
        return names;
    }

    @Override
    public List<? extends HtmlElement> getElements() {
        Assert.isTrue((text == null || textItem == null) && (text != null || textItem != null));
        if (text == null) {
            return Collections.singletonList(textItem);
        } else {
            return Collections.singletonList(text);
        }
    }

    public Text() {
        super();
    }

    public Text(TextItem textItem) {
        super();
        this.textItem = textItem;
    }

    public Text(Text text) {
        super();
        this.text = text;
    }

    public TextItem getTextItem() {
        return textItem;
    }

    public void setTextItem(TextItem textItem) {
        this.textItem = textItem;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
