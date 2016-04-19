package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.split;

public class ColorRuleExtractor {
    public List<ColorRule> getColorRules(String colorRules) {
        List<ColorRule> result = new ArrayList<ColorRule>();
        if (colorRules == null) {
            return result;
        }
        try {
            for (String rule : split(colorRules, ":")) {
                String[] split = split(rule, "|");
                if (split != null && split.length == 3) {
                    result.add(new ColorRule(
                        ColorPatternTarget.valueOf(split[0]),
                        split[1],
                        split[2]
                    ));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("colorRules not correctly configured: format: " + getFormat());
        }
        return result;
    }

    private String getFormat() {
        return "target|searchPattern|color:target2|searchPettern2|color2:...:targetN|searchPatternN|colorN  (with target in (ARTIFACT, GROUP, VERSION, SCOPE, TYPE, CLASSIFIER))";
    }

    public static class ColorRule {
        ColorPatternTarget target;
        String pattern;
        String color;

        private ColorRule(ColorPatternTarget target, String pattern, String color) {
            this.target = target;
            this.pattern = pattern;
            this.color = color;
        }

        public ColorPatternTarget getTarget() {
            return target;
        }

        public String getPattern() {
            return pattern;
        }

        public String getColor() {
            return color;
        }
    }

    public enum ColorPatternTarget {
        ARTIFACT, GROUP, VERSION, SCOPE, TYPE, CLASSIFIER
    }
}
