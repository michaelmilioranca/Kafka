package com.alura.ecommerce;

import java.io.Closeable;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

// The Closeable interface is a fail safe to close the IO if something fails
public class KafkaDispatcher<T> implements Closeable {

  final KafkaProducer<String, T> producer;

  public KafkaDispatcher() {
    this.producer = new KafkaProducer<>(getProperties());
  }

  private Properties getProperties() {
    Properties properties = new Properties();
    // Where the kafka is running
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    // The class to serialize the KEY value of the message
    properties.setProperty(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    // The class to serialize the VALUE of the message
    properties.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
    return properties;
  }

  public void send(final String topic, final String key, final T value)
      throws ExecutionException, InterruptedException {
    var record = new ProducerRecord<>(topic, key, value);
    // The send is assyncronous, if we want to something with the "return" of it, we have to use get
    // The send also can receive a callback, which is has de data and the exception
    producer.send(record, getDefaultCallback()).get();
  }

  private Callback getDefaultCallback() {
    return (data, ex) -> {
      if (Objects.nonNull(ex)) {
        ex.printStackTrace();
        return;
      }
      System.out.println(
          "Message sent to "
              + data.topic()
              + ":::partition"
              + data.partition()
              + "/.offseet"
              + data.offset()
              + "/.timestamp"
              + data.timestamp());
    };
  }

  @Override
  public void close() {
    producer.close();
  }
}
