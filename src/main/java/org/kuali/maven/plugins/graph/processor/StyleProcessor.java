/**
 * Copyright 2011-2013 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.pojo.Style;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class StyleProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(StyleProcessor.class);
    TreeHelper helper = new TreeHelper();
    Properties properties;

    public StyleProcessor(GraphDescriptor gd)
    {
        final Properties defaultProperties = getProperties();
        this.properties = new Properties(defaultProperties);

        Set<String> legalNames = defaultProperties.stringPropertyNames();

        // copy only the properties from the pom that are also in the default dot.properties file.
        for (String propertyName : gd.getStyleProperties().stringPropertyNames()) {
            if (legalNames.contains(propertyName)) {
                this.properties.setProperty(propertyName, gd.getStyleProperties().getProperty(propertyName));
            }
        }
    }

    @Override
    public void process(Node<MavenContext> node) {
        List<Node<MavenContext>> list = node.getBreadthFirstList();
        for (Node<MavenContext> element : list) {
            updateGraphNodeStyle(element.getObject());
        }
    }

    protected static Properties getProperties() {
        String location = "classpath:dot.properties";
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(location);
        InputStream in = null;
        try {
            Properties properties = new Properties();
            in = resource.getInputStream();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    protected String getStyle(String property, Scope scope, boolean optional, State state) {
        // State styling overrides everything
        String key1 = "state." + state.getValue() + "." + property;
        // Scope styling overrides "optional" styling
        String key2 = "scope." + scope.getValue() + "." + property;
        // Fall through to styling for the "optional" attribute on a dependency
        String key3 = "optional." + property;

        String value1 = properties.getProperty(key1);
        String value2 = properties.getProperty(key2);
        String value3 = properties.getProperty(key3);

        if (!Helper.isBlank(value1)) {
            return value1;
        } else if (!Helper.isBlank(value2)) {
            return value2;
        } else if (!Helper.isBlank(value3) && optional) {
            return value3;
        } else {
            return null;
        }

    }

    protected List<String> getStyleProperties() {
        try {
            @SuppressWarnings("unchecked")
            Map<String, ?> map = BeanUtils.describe(Style.DEFAULT_STYLE);
            map.remove("class");
            return new ArrayList<String>(map.keySet());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Style getStyle(Scope scope, boolean optional, State state) {
        // This happens for the root node
        scope = (scope == null) ? Scope.DEFAULT_SCOPE : scope;
        state = (state == null) ? State.INCLUDED : state;

        List<String> properties = getStyleProperties();
        Style style = new Style();

        for (String property : properties) {
            String value = getStyle(property, scope, optional, state);
            if (Helper.isBlank(value)) {
                continue;
            }
            helper.copyProperty(style, property, value);
        }
        return style;
    }

    public void updateGraphNodeStyle(MavenContext context) {
        DependencyNode dn = context.getDependencyNode();
        boolean optional = context.isOptional();
        State state = context.getState();
        Scope scope = Scope.getScope(dn.getArtifact().getScope());
        Style style = getStyle(scope, optional, state);
        if (optional) {
            logger.debug("optional {}, style={}", context.getArtifactIdentifier(), style.getStyle());
        }
        copyStyleProperties(context.getGraphNode(), style);
        if (optional) {
            context.getGraphNode().setStyle("dotted,filled");
        } else {
            context.getGraphNode().setStyle("solid,filled");
        }
    }

    public void copyStyleProperties(Object dest, Style style) {
        List<String> names = getStyleProperties();
        for (String name : names) {
            String value = helper.getProperty(style, name);
            if (!Helper.isBlank(value)) {
                helper.copyProperty(dest, name, value);
            }
        }
    }

}
