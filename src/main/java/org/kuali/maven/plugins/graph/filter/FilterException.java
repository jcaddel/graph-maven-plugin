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
package org.kuali.maven.plugins.graph.filter;

public class FilterException extends RuntimeException {

    private static final long serialVersionUID = 7631598834023805362L;

    public FilterException() {
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(Throwable exception) {
        super(exception);
    }

    public FilterException(String message, Throwable exception) {
        super(message, exception);
    }

}
