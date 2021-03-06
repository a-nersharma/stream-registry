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
package com.expediagroup.streamplatform.streamregistry.core.services;

import static java.util.stream.StreamSupport.stream;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.expediagroup.streamplatform.streamregistry.core.handlers.HandlerService;
import com.expediagroup.streamplatform.streamregistry.core.repositories.ProducerRepository;
import com.expediagroup.streamplatform.streamregistry.core.validators.ProducerValidator;
import com.expediagroup.streamplatform.streamregistry.model.Producer;
import com.expediagroup.streamplatform.streamregistry.model.keys.ProducerKey;

@Component
@RequiredArgsConstructor
public class ProducerService {
  private final HandlerService handlerService;
  private final ProducerValidator producerValidator;
  private final ProducerRepository producerRepository;

  public Optional<Producer> create(Producer producer) throws ValidationException {
    if (producerRepository.findById(producer.getKey()).isPresent()) {
      throw new ValidationException("Can't create because it already exists");
    }
    producerValidator.validateForCreate(producer);
    producer.setSpecification(handlerService.handleInsert(producer));
    return Optional.ofNullable(producerRepository.save(producer));
  }

  public Optional<Producer> read(ProducerKey key) {
    return producerRepository.findById(key);
  }

  public Iterable<Producer> readAll() {
    return producerRepository.findAll();
  }

  public Optional<Producer> update(Producer producer) throws ValidationException {
    Optional<Producer> existing = producerRepository.findById(producer.getKey());
    if (!existing.isPresent()) {
      throw new ValidationException("Can't update because it doesn't exist");
    }
    producerValidator.validateForUpdate(producer, existing.get());
    producer.setSpecification(handlerService.handleUpdate(producer, existing.get()));
    return Optional.ofNullable(producerRepository.save(producer));
  }

  public Optional<Producer> upsert(Producer producer) throws ValidationException {
    return !producerRepository.findById(producer.getKey()).isPresent() ?
        create(producer) :
        update(producer);
  }

  public void delete(Producer producer) {
    throw new UnsupportedOperationException();
  }

  public Iterable<Producer> findAll(Predicate<Producer> filter) {
    return stream(producerRepository.findAll().spliterator(), false)
        .filter(r -> filter.test(r))
        .collect(Collectors.toList());
  }

  public void validateProducerBindingExists(ProducerKey key) {
    if (read(key).isEmpty()) {
      throw new ValidationException("Producer does not exist");
    }
  }
}