/**
 * Copyright (C) 2018-2020 Expedia, Inc.
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
package com.expediagroup.streamplatform.streamregistry.graphql.mutation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLMutationResolver;

@Component
@RequiredArgsConstructor
@Getter
@PreAuthorize("isAuthenticated()")
public class Mutation implements GraphQLMutationResolver {
  private final DomainMutation domain;
  private final SchemaMutation schema;
  private final StreamMutation stream;
  private final ConsumerMutation consumer;
  private final ProducerMutation producer;
  private final ZoneMutation zone;
  private final InfrastructureMutation infrastructure;
  private final StreamBindingMutation streamBinding;
  private final ProducerBindingMutation producerBinding;
  private final ConsumerBindingMutation consumerBinding;
}
