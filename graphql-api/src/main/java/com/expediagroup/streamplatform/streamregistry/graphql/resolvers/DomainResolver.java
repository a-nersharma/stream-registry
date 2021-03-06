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
package com.expediagroup.streamplatform.streamregistry.graphql.resolvers;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.coxautodev.graphql.tools.GraphQLResolver;

import org.springframework.stereotype.Component;

import com.expediagroup.streamplatform.streamregistry.core.services.SchemaService;
import com.expediagroup.streamplatform.streamregistry.model.Domain;
import com.expediagroup.streamplatform.streamregistry.model.Schema;
import com.expediagroup.streamplatform.streamregistry.model.Status;

@Component
@RequiredArgsConstructor
public class DomainResolver implements GraphQLResolver<Domain> {
  private final SchemaService schemaService;

  public List<Schema> schemas(Domain domain) {
    List<Schema> out = new ArrayList<>();
    for (Schema v : schemaService.readAll()) {
      if (v.getKey().getDomain().equals(domain.getKey().getName())) {
        out.add(v);
      }
    }
    return out;
  }

  public Status status(Domain domain) {
    return domain.getStatus() == null ? new Status() : domain.getStatus();
  }
}
