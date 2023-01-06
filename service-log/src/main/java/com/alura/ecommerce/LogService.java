package com.alura.ecommerce;

import static com.alura.ecommerce.TopicConstants.ECOMMERCE_ALL_TOPICS;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {

  public static void main(String[] args) {
    var logService = new LogService();
    try (var kafkaService =
        KafkaServiceCreator.groupId(LogService.class.getSimpleName())
            .topic(Pattern.compile(ECOMMERCE_ALL_TOPICS))
            .parse(logService::parse)
            .type(String.class)
            .properties(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
            .create()
    //        new KafkaService.Builder()
    //            .groupId(LogService.class.getSimpleName())
    //            .patternTopic(Pattern.compile(ECOMMERCE_ALL_TOPICS))
    //            .parse(logService::parse)
    //            .type(String.class)
    //            .properties(VALUE_DESERIALIZER_CLASS_CONFIG, String.class.getSimpleName())
    //            .build()
    ) {
      kafkaService.run();
    }
  }

  private void parse(final ConsumerRecord<String, String> record) {
    System.out.println("---------------------------------------");
    System.out.println("LOG: Topic " + record.topic());
    System.out.println(record.key());
    System.out.println(record.value());
    System.out.println(record.partition());
    System.out.println(record.offset());
    System.out.println("Email sent!");
  }
}
