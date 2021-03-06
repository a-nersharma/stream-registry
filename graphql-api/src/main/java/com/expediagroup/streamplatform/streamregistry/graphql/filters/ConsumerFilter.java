/**
 * Copyright (C) 2018-2019 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.streamplatform.streamregistry.graphql.filters;

import static com.expediagroup.streamplatform.streamregistry.graphql.filters.FilterUtility.matches;
import static com.expediagroup.streamplatform.streamregistry.graphql.filters.SpecificationMatchUtility.matchesSpecification;

import java.util.function.Predicate;

import com.expediagroup.streamplatform.streamregistry.graphql.model.queries.ConsumerKeyQuery;
import com.expediagroup.streamplatform.streamregistry.graphql.model.queries.SpecificationQuery;
import com.expediagroup.streamplatform.streamregistry.model.Consumer;

public class ConsumerFilter implements Predicate<Consumer> {

  private final ConsumerKeyQuery keyQuery;
  private final SpecificationQuery specQuery;

  public ConsumerFilter(ConsumerKeyQuery keyQuery, SpecificationQuery specQuery) {
    this.keyQuery = keyQuery;
    this.specQuery = specQuery;
  }

  @Override
  public boolean test(Consumer consumer) {
    if (keyQuery != null) {
      if (!matches(consumer.getKey().getName(), keyQuery.getNameRegex())) {
        return false;
      }
      if (!matches(consumer.getKey().getStreamDomain(), keyQuery.getStreamDomainRegex())) {
        return false;
      }
      if (!matches(consumer.getKey().getStreamName(), keyQuery.getStreamNameRegex())) {
        return false;
      }
      if (!matches(consumer.getKey().getZone(), keyQuery.getZoneRegex())) {
        return false;
      }
      if (keyQuery.getStreamVersion() != null && consumer.getKey().getStreamVersion() != keyQuery.getStreamVersion()) {
        return false;
      }
    }
    return matchesSpecification(consumer.getSpecification(), specQuery);
  }
}
