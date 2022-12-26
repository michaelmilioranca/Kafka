package com.alura.ecommerce;

import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_ALL_TOPICS;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.regex.Pattern;

public class LogService {

  public static void main(String[] args) {
    var logService = new LogService();
    try (var kafkaService =
        new KafkaService(
            LogService.class.getSimpleName(), Pattern.compile(ECOMMERCE_ALL_TOPICS), logService::parse)) {
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
