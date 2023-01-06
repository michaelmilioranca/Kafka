package com.alura.ecommerce;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class KafkaServiceFluent
    implements KafkaServiceCreator.Properties,
        KafkaServiceCreator.Parse,
        KafkaServiceCreator.Type,
        KafkaServiceCreator.Topic,
        KafkaServiceCreator.Create {
  private String groupId;
  private ConsumerFunction parse;
  private Class type;
  private Map<String, String> overrideProperties = new HashMap<>();
  private Pattern patternTopic;
  private String topic;

  KafkaServiceFluent(String groupId) {
    this.groupId = groupId;
  }

  @Override
  public KafkaServiceCreator.Parse topic(Pattern topic) {
    this.patternTopic = topic;
    return this;
  }

  @Override
  public KafkaServiceCreator.Parse topic(String topic) {
    this.topic = topic;
    return this;
  }

  @Override
  public KafkaServiceCreator.Type parse(ConsumerFunction parse) {
    this.parse = parse;
    return this;
  }

  @Override
  public KafkaServiceCreator.Properties type(Class type) {
    this.type = type;
    return this;
  }

  @Override
  public KafkaServiceCreator.Create properties(Map<String, String> overrideProperties) {
    this.overrideProperties.putAll(overrideProperties);
    return this;
  }

  @Override
  public KafkaServiceCreator.Create properties(String key, String value) {
    this.overrideProperties.put(key, value);
    return this;
  }

  @Override
  public KafkaService create() {
    if (Objects.isNull(topic)) {
      return new KafkaService(
          this.groupId, this.patternTopic, this.parse, this.type, this.overrideProperties);
    }
    return new KafkaService(
        this.groupId, this.topic, this.parse, this.type, this.overrideProperties);
  }
}
