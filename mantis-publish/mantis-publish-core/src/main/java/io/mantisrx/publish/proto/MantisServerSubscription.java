/*
 * Copyright 2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mantisrx.publish.proto;

import io.mantisrx.shaded.com.fasterxml.jackson.annotation.JsonCreator;
import io.mantisrx.shaded.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.mantisrx.shaded.com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MantisServerSubscription {

    private final String query;
    private final String subscriptionId;
    private final Map<String, String> additionalParams;

    public MantisServerSubscription(String subId, String query) {
        this(subId, query, new HashMap<>());
    }

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
    public MantisServerSubscription(@JsonProperty("subscriptionId") String subId,
                                    @JsonProperty("query") String query,
                                    @JsonProperty("additionalParams") Map<String, String> additionalParams) {
        this.query = query;
        this.additionalParams = additionalParams;
        this.subscriptionId = subId;

    }

    public String getQuery() {
        return query;
    }

    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MantisServerSubscription that = (MantisServerSubscription) o;
        // Check subscriptionId first (likely to differ) to fail fast
        if (subscriptionId != null) {
            if (!subscriptionId.equals(that.subscriptionId)) return false;
        } else if (that.subscriptionId != null) {
            return false;
        }
        // Then check query
        if (query != null) {
            if (!query.equals(that.query)) return false;
        } else if (that.query != null) {
            return false;
        }
        // Finally check additionalParams, use reference equality as a fast path
        if (additionalParams == that.additionalParams) return true;
        if (additionalParams != null) return additionalParams.equals(that.additionalParams);
        return that.additionalParams == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, subscriptionId, additionalParams);
    }

    @Override
    public String toString() {
        return "MantisServerSubscription{"
                + " query='" + query + '\''
                + ", subscriptionId='" + subscriptionId + '\''
                + ", additionalParams=" + additionalParams
                + '}';
    }
}
