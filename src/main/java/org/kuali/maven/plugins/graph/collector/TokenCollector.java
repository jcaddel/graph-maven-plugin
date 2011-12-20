package org.kuali.maven.plugins.graph.collector;

import java.util.List;

public interface TokenCollector<T> {

    List<String> getTokens(T object);

}
