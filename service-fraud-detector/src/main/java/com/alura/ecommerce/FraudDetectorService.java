package com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {

  public static void main(String[] args) {
    var fraudeDectectorService = new FraudDetectorService();
    try (var kafkaService =
        new KafkaService.Builder()
            .groupId(FraudDetectorService.class.getSimpleName())
            .topic(TopicConstants.ECOMMERCE_TOPIC)
            .parse(fraudeDectectorService::parse)
            .type(Order.class.getSimpleName())
            .build()) {
      kafkaService.run();
    }
  }

  private void parse(ConsumerRecord<String, Order> record) {
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
