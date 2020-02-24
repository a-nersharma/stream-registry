package com.expediagroup.streamplatform.streamregistry.core.services;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import com.expediagroup.streamplatform.streamregistry.model.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.expediagroup.streamplatform.streamregistry.core.handlers.HandlerService;
import com.expediagroup.streamplatform.streamregistry.core.repositories.StreamRepository;
import com.expediagroup.streamplatform.streamregistry.core.validators.StreamValidator;
import com.expediagroup.streamplatform.streamregistry.model.keys.StreamKey;

@RunWith(MockitoJUnitRunner.class)
public class StreamServiceTest {

  private static final String TEST_DOMAIN = "domain";
  private static final String TEST_STREAM_NAME = "streamName";
  @Mock
  private HandlerService handlerService;

  @Mock
  private StreamValidator streamValidator;

  @Mock
  private StreamRepository streamRepository;

  @InjectMocks
  private StreamService streamService;

  @Test
  public void create() {
  }

  @Test
  public void read() {
    StreamKey streamKey = StreamKey.builder().domain(TEST_DOMAIN).name(TEST_STREAM_NAME).build();
    Stream stream = Stream.builder().key(streamKey).build();

    when(streamRepository.findById(streamKey)).thenReturn(Optional.of(stream));

    Stream readStream = streamService.read(streamKey).get();

    assertEquals(stream, readStream);
    verify(streamRepository).findById(streamKey);
  }

  @Test
  public void readAll() {
    StreamKey streamKey = StreamKey.builder().domain(TEST_DOMAIN).name(TEST_STREAM_NAME).build();
    Stream stream = Stream.builder().key(streamKey).build();

    when(streamRepository.findAll()).thenReturn(asList(stream));

    Iterable<Stream> streamIterable = streamService.readAll();

    assertEquals(asList(stream), streamIterable);
    verify(streamRepository).findAll();
  }

  @Test
  public void update() {
  }

  @Test
  public void upsert() {
  }

  @Test(expected = UnsupportedOperationException.class)
  public void delete() {
    StreamKey streamKey = StreamKey.builder().domain(TEST_DOMAIN).name(TEST_STREAM_NAME).build();
    Stream stream = Stream.builder().key(streamKey).build();

    streamService.delete(stream);
  }

  @Test
  public void findAll() {
  }

  @Test(expected = ValidationException.class)
  public void validateStreamExists() {

    StreamKey streamKey = StreamKey.builder().domain(TEST_DOMAIN).name(TEST_STREAM_NAME).build();

    when(streamRepository.findById(streamKey)).thenReturn(Optional.empty());

    streamService.validateStreamExists(streamKey);
  }

}