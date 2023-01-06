package com.alura.ecommerce;

import java.util.Map;
import java.util.regex.Pattern;

public interface KafkaServiceCreator {

  interface Topic {
    Parse topic(Pattern topic);

    Parse topic(String topic);
  }

  interface Parse {
    Type parse(ConsumerFunction parse);
  }

  interface Type {
    Properties type(Class type);
  }

  interface Properties {
    Create properties(Map<String, String> overrideProperties);

    Create properties(String key, String value);

    KafkaService create();
  }

  interface Create {
    KafkaService create();
  }

  static Topic groupId(String groupId) {
    return new KafkaServiceFluent(groupId);
  }
}
