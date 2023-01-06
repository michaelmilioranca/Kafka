package com.alura.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService<T> implements Closeable {

  private final KafkaConsumer<String, T> consumer;
  private final ConsumerFunction parse;

  protected KafkaService(
      final String groupId,
      final String topic,
      final ConsumerFunction parse,
      final Class<T> type,
      Map<String, String> overrideProperties) {
    this(groupId, parse, type, overrideProperties);
    consumer.subscribe(Collections.singletonList(topic));
  }

  protected KafkaService(
      final String groupId,
      final Pattern topic,
      final ConsumerFunction parse,
      final Class<T> type,
      Map<String, String> overrideProperties) {
    this(groupId, parse, type, overrideProperties);
    consumer.subscribe(topic);
  }

  private KafkaService(
      final String groupId,
      final ConsumerFunction parse,
      final Class<T> type,
      Map<String, String> overrideProperties) {
    this.consumer = new KafkaConsumer<>(getProperties(groupId, type, overrideProperties));
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

  private Properties getProperties(
      final String groupId, final Class<T> type, Map<String, String> overrideProperties) {
    var properties = new Properties();
    // Where the kafka is running
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    // Class used to DESERIALIZE the key
    properties.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    // Class used to DESERIALIZE the value
    properties.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
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
    // This is a workaround to have a generic deserializer in our custom class GsonDeserializer
    properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
    // Overrides the current configs to the desired ones
    properties.putAll(overrideProperties);
    return properties;
  }

  @Override
  public void close() {
    consumer.close();
  }

  public static class Builder {
    private String groupId;
    private ConsumerFunction parse;
    private Class type;
    private Map<String, String> overrideProperties = new HashMap<>();
    private Pattern patternTopic;
    private String topic;

    public Builder() {}

    public Builder groupId(final String groupId) {
      this.groupId = groupId;
      return this;
    }

    public Builder topic(final String topic) {
      this.topic = topic;
      return this;
    }

    public Builder patternTopic(final Pattern patternTopic) {
      this.patternTopic = patternTopic;
      return this;
    }

    public Builder parse(final ConsumerFunction parse) {
      this.parse = parse;
      return this;
    }

    public Builder type(final Class type) {
      this.type = type;
      return this;
    }

    public Builder properties(final Map<String, String> overrideProperties) {
      this.overrideProperties.putAll(overrideProperties);
      return this;
    }

    public Builder properties(String key, String value) {
      this.overrideProperties.put(key, value);
      return this;
    }

    public KafkaService build() {
      if (Objects.isNull(this.patternTopic)) {
        return new KafkaService(
            this.groupId, this.topic, this.parse, String.class, this.overrideProperties);
      }
      return new KafkaService(
          this.groupId, this.patternTopic, this.parse, String.class, this.overrideProperties);
    }
  }
}
