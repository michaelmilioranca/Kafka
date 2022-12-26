package com.alura.ecommerce;

import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_TOPIC;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {

  public static void main(String[] arigs) {
    var fraudeDectectorService = new FraudDetectorService();
    try (var kafkaService =
        new KafkaService(
            FraudDetectorService.class.getSimpleName(),
            ECOMMERCE_TOPIC,
            fraudeDectectorService::parse)) {
      kafkaService.run();
    }
  }

  private void parse(ConsumerRecord<String, String> record) {
    System.out.println("---------------------------------------");
    System.out.println("Processing new order, checking fraud...");
    System.out.println(record.key());
    System.out.println(record.value());
    System.out.println(record.partition());
    System.out.println(record.offset());
    // Simulate something running
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      // Ignoring
      e.printStackTrace();
    }
    System.out.println("Order processed");
  }
}
