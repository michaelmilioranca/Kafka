package com.alura.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService implements Closeable {

  private final KafkaConsumer<String, String> consumer;
  private final ConsumerFunction parse;

  public KafkaService(final String groupId, final String topic, final ConsumerFunction parse) {
    this(groupId, parse);
    consumer.subscribe(Collections.singletonList(topic));
  }

  public KafkaService(String groupId, Pattern topic, ConsumerFunction parse) {
    this(groupId, parse);
    consumer.subscribe(topic);
  }

  private KafkaService(String groupId, ConsumerFunction parse) {
    this.consumer = new KafkaConsumer<>(getProperties(groupId));
    this.parse = parse;
  }

  public void run() {
    while (true) {
      // The poll will return a list of records given the duration of the "wait"
      var records = consumer.poll(Duration.ofMillis(100));
      if (!records.isEmpty()) {
        // this should be always one since we setted to have max poll of ONE
        System.out.println("Found " + records.count() + " registries");
        for (var record : records) {
          parse.consume(record);
        }
      }
    }
  }

  private static Properties getProperties(final String groupId) {
    var properties = new Properties();
    // Where the kafka is running
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    // Class used to DESERIALIZE the key
    properties.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    // Class used to DESERIALIZE the value
    properties.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    // The group ID is neeeded and not often repeated between projects
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    // The client ID is showed when we use the --describe of the consumers it will be easier to
    // identify which app is consuming
    // Also it needs to be unique
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
    // To avoid having loss of records due to auto rebalance of the kafka, we can set to "grab" one
    // record at time
    // This makes we commit each record we read instead of waiting them all to commit
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    return properties;
  }

  @Override
  public void close() {
    consumer.close();
  }
}
