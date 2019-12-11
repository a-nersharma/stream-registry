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
package com.expediagroup.streamplatform.streamregistry.it;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import com.expediagroup.streamplatform.streamregistry.graphql.client.CreateSessionMutation;
import com.expediagroup.streamplatform.streamregistry.graphql.client.RenewSessionMutation;
import com.expediagroup.streamplatform.streamregistry.it.helpers.AbstractTest;

public class SessionTestStage extends AbstractTest {

  @Test
  public void create() {
    setFactorySuffix("create");

    CreateSessionMutation.Create create = createSession();

    assertNotNull(create);
  }

  @Test
  public void renew() {
    CreateSessionMutation.Create session = createSession();

    Object result = client.getOptionalData(
        factory.renewSessionMutationBuilder(session.getId().get(), session.getSecret()).build()).get();

    RenewSessionMutation.Session renewedSession = ((RenewSessionMutation.Data) result).getSession();

    assertThat(renewedSession.getRenew().getId(), is(session.getId()));
    assertThat(renewedSession.getRenew().getSecret(), is(session.getSecret()));
  }

  @Test
  public void shouldFailRenewOnExpiredSession() throws InterruptedException {
    CreateSessionMutation.Create session = createSession();

    Thread.sleep(Long.parseLong(getConfigProperty("session-expiration-in-ms")));

    assertRequiresObjectIsAbsent((factory.renewSessionMutationBuilder(session.getId().get(), session.getSecret()).build()),
            "The session is expired. Please create a new one to access the resource.");
  }

  @NotNull
  private CreateSessionMutation.Create createSession() {
    Object data = client.getOptionalData(factory.insertSessionMutationBuilder().build()).get();

    return ((CreateSessionMutation.Data) data).getSession().getCreate();
  }

  @Override
  public void createRequiredDatastoreState() {
    client.createProducer(factory);
    client.createConsumer(factory);
    client.createProducerBinding(factory);
    client.createConsumerBinding(factory);
  }

}
