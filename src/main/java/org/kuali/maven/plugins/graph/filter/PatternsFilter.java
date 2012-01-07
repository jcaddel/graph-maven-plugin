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
package org.kuali.maven.plugins.graph.filter;

import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.kuali.maven.plugins.graph.collector.TokenCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Filter to match objects against a list of patterns.
 * </p>
 *
 * The default pattern syntax has the form:
 *
 * <pre>
 * [token1]:[token2]:[token3]
 * </pre>
 *
 * <p>
 * Each segment is optional and supports <code>*</code> wildcards. An empty segment is treated as a wildcard.
 * </p>
 *
 */
public class PatternsFilter<T> implements Filter<T> {
    private static final Logger logger = LoggerFactory.getLogger(PatternsFilter.class);

    public static final String DEFAULT_DELIMITER = ":";
    public static final String DEFAULT_WILDCARD = "*";

    String delimiter = DEFAULT_DELIMITER;
    String wildcard = DEFAULT_WILDCARD;

    /**
     * The list of artifact patterns to match, as described above.
     */
    List<String> patterns;

    /**
     * The collector examines an object and supplies tokens to match against the patterns
     */
    TokenCollector<T> collector;

    public PatternsFilter() {
        this(null);
    }

    public PatternsFilter(List<String> patterns) {
        this(patterns, null);
    }

    public PatternsFilter(List<String> patterns, TokenCollector<T> collector) {
        super();
        this.patterns = patterns;
        this.collector = collector;
    }

    @Override
    public boolean isMatch(T object) {

        // If they didn't supply patterns, it implies "matches everything"
        if (isEmpty(patterns)) {
            return true;
        }

        // Collect string tokens from the object
        List<String> tokens = collector.getTokens(object);
        logger.debug("tokens size={}", tokens.size());

        // Make sure the patterns are valid
        validate(tokens, patterns);

        // Determine if there is a match with a pattern
        return isAtLeastOneMatch(tokens, patterns);
    }

    /**
     * Return true if the tokens match one of the patterns
     */
    protected boolean isAtLeastOneMatch(List<String> tokens, List<String> patterns) {

        // Cycle through the patterns looking for a match
        for (String pattern : patterns) {

            // We matched a pattern, we're done
            if (isMatch(tokens, pattern)) {
                return true;
            }
        }

        logger.debug("no match");

        // The artifact didn't match any of the patterns
        return false;
    }

    protected boolean isMatch(List<String> tokens, String pattern) {
        // Split the pattern into tokens
        String[] patternTokens = pattern.split(delimiter);

        // Cycle through the tokens in the pattern
        for (int i = 0; i < patternTokens.length; i++) {
            String token = tokens.get(i);

            // If any token does not match its corresponding pattern, we're done
            if (!isMatch(token, patternTokens[i])) {
                logger.debug("{} does not match {}", token, patternTokens[i]);
                return false;
            }
        }

        // All the tokens matched their corresponding pattern
        return true;
    }

    /**
     * Determine if the specified token matches the specified pattern segment.
     *
     * @param token
     *            the token to check
     * @param pattern
     *            the pattern segment to match, as defined above
     * @return <code>true</code> if the specified token is matched by the specified pattern segment
     */
    protected boolean isMatch(String token, String pattern) {

        // full wildcard or implied wildcard
        if (wildcard.equals(pattern) || pattern.length() == 0) {
            return true;
        }

        // We need a positive match with the pattern
        if (token == null) {
            return false;
        }

        // contains wildcard - *foo*
        if (pattern.startsWith(wildcard) && pattern.endsWith(wildcard)) {
            String contains = pattern.substring(1, pattern.length() - 1);
            return token.indexOf(contains) != -1;
        }

        // leading wildcard - *foo
        if (pattern.startsWith(wildcard)) {
            String suffix = pattern.substring(1, pattern.length());
            return token.endsWith(suffix);
        }

        // trailing wildcard - foo*
        if (pattern.endsWith(wildcard)) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return token.startsWith(prefix);
        }

        // versions range
        if (pattern.startsWith("[") || pattern.startsWith("(")) {
            return isVersionIncludedInRange(token, pattern);
        }

        // exact match
        return token.equals(pattern);
    }

    protected boolean isVersionIncludedInRange(String version, String range) {
        try {
            VersionRange versionRange = VersionRange.createFromVersionSpec(range);
            ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
            return versionRange.containsVersion(artifactVersion);
        } catch (InvalidVersionSpecificationException e) {
            throw new FilterException(e);
        }
    }

    protected boolean isEmpty(Collection<?> c) {
        return c == null || c.size() == 0;
    }

    protected boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    protected void validate(List<String> artifactTokens, List<String> patterns) {
        for (String pattern : patterns) {
            if (!isValid(artifactTokens, pattern)) {
                throw new FilterException("Invalid pattern: '" + pattern + "'");
            }
        }
    }

    protected boolean isValid(List<String> artifactTokens, String pattern) {
        if (isBlank(pattern)) {
            return false;
        }

        String[] tokens = pattern.split(delimiter);
        int length = tokens.length;
        return length <= artifactTokens.size();
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public TokenCollector<T> getCollector() {
        return collector;
    }

    public void setCollector(TokenCollector<T> collector) {
        this.collector = collector;
    }

}
