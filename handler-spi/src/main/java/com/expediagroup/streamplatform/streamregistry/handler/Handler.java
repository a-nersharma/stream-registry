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
package com.expediagroup.streamplatform.streamregistry.handler;

import com.expediagroup.streamplatform.streamregistry.model.ManagedType;
import com.expediagroup.streamplatform.streamregistry.model.Specification;

public interface Handler<T extends ManagedType> {
  String type();

  Class<T> target();

  Specification handleInsert(T entity);

  Specification handleUpdate(T entity, T existing);
}
