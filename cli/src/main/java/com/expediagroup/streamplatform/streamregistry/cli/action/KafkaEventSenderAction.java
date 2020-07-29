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
package com.expediagroup.streamplatform.streamregistry.cli.action;

import lombok.Getter;
import picocli.CommandLine.Option;

import com.expediagroup.streamplatform.streamregistry.state.EventSender;
import com.expediagroup.streamplatform.streamregistry.state.kafka.KafkaEventSender;

public abstract class KafkaEventSenderAction implements EventSenderAction {
  @Option(names = "--bootstrapServers", required = true)
  @Getter String bootstrapServers;
  @Option(names = "--topic", required = true, defaultValue = "_streamregistry")
  @Getter String topic;
  @Option(names = "--schemaRegistryUrl", required = true)
  @Getter String schemaRegistryUrl;

  @Override
  public EventSender sender() {
    KafkaEventSender.Config config = KafkaEventSender.Config.builder()
        .bootstrapServers(bootstrapServers)
        .topic(topic)
        .schemaRegistryUrl(schemaRegistryUrl)
        .build();
    return new KafkaEventSender(config);
  }
}