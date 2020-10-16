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
package com.expediagroup.streamplatform.streamregistry.core.services;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.expediagroup.streamplatform.streamregistry.core.handlers.HandlerService;
import com.expediagroup.streamplatform.streamregistry.core.validators.StreamBindingValidator;
import com.expediagroup.streamplatform.streamregistry.core.validators.ValidationException;
import com.expediagroup.streamplatform.streamregistry.model.Status;
import com.expediagroup.streamplatform.streamregistry.model.StreamBinding;
import com.expediagroup.streamplatform.streamregistry.model.keys.StreamBindingKey;
import com.expediagroup.streamplatform.streamregistry.repository.StreamBindingRepository;

@Component
@RequiredArgsConstructor
public class StreamBindingService {
  private final HandlerService handlerService;
  private final StreamBindingValidator streamBindingValidator;
  private final StreamBindingRepository streamBindingRepository;

  @PreAuthorize("hasPermission(#streamBinding, 'CREATE')")
  public Optional<StreamBinding> create(StreamBinding streamBinding) throws ValidationException {
    if (read(streamBinding.getKey()).isPresent()) {
      throw new ValidationException("Can't create because it already exists");
    }
    streamBindingValidator.validateForCreate(streamBinding);
    streamBinding.setSpecification(handlerService.handleInsert(streamBinding));
    return save(streamBinding);
  }

  @PreAuthorize("hasPermission(#streamBinding, 'UPDATE')")
  public Optional<StreamBinding> update(StreamBinding streamBinding) throws ValidationException {
    var existing = read(streamBinding.getKey());
    if (!existing.isPresent()) {
      throw new ValidationException("Can't update " + streamBinding.getKey() + " because it doesn't exist");
    }
    streamBindingValidator.validateForUpdate(streamBinding, existing.get());
    streamBinding.setSpecification(handlerService.handleUpdate(streamBinding, existing.get()));
    return save(streamBinding);
  }

  @PreAuthorize("hasPermission(#streamBindingKey, 'UPDATE_STATUS')")
  public Optional<StreamBinding> updateStatus(StreamBindingKey streamBindingKey, Status status) {
    StreamBinding streamBinding = read(streamBindingKey).get();
    streamBinding.setStatus(status);
    return save(streamBinding);
  }

  private Optional<StreamBinding> save(StreamBinding streamBinding) {
    streamBinding = streamBindingRepository.save(streamBinding);
    return Optional.ofNullable(streamBinding);
  }

  public Optional<StreamBinding> upsert(StreamBinding streamBinding) throws ValidationException {
    return !read(streamBinding.getKey()).isPresent() ? create(streamBinding) : update(streamBinding);
  }

  public Optional<StreamBinding> read(StreamBindingKey key) {
    return streamBindingRepository.findById(key);
  }

  public List<StreamBinding> findAll(Predicate<StreamBinding> filter) {
    return streamBindingRepository.findAll().stream().filter(filter).collect(toList());
  }

  @PreAuthorize("hasPermission(#streamBinding, 'DELETE')")
  public void delete(StreamBinding streamBinding) {
    throw new UnsupportedOperationException();
  }

  public boolean exists(StreamBindingKey key) {
    return read(key).isPresent();
  }
}
