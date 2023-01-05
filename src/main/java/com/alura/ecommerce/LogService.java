package com.alura.ecommerce;

import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_ALL_TOPICS;

import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class LogService {

  public static void main(String[] args) {
    var logService = new LogService();
    try (var kafkaService =
        new KafkaService.Builder()
            .groupId(FraudDetectorService.class.getSimpleName())
            .patternTopic(Pattern.compile(ECOMMERCE_ALL_TOPICS))
            .parse(logService::parse)
            .type(String.class.getSimpleName())
            .properties(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, String.class.getSimpleName())
            .build()) {
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
