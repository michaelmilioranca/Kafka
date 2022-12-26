package com.alura.ecommerce;

import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_TOPIC_SEND_EMAIL;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

  public static void main(String[] args) {
    var emailService = new EmailService();
    try (var kafkaService =
        new KafkaService(
            EmailService.class.getSimpleName(), ECOMMERCE_TOPIC_SEND_EMAIL, emailService::parse)) {
      kafkaService.run();
    }
  }

  private void parse(ConsumerRecord<String, String> record) {
    System.out.println("---------------------------------------");
    System.out.println("Sending email...");
    System.out.println(record.key());
    System.out.println(record.value());
    System.out.println(record.partition());
    System.out.println(record.offset());
    // Just to "simulate" that its running something instead of just printing stuff
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      // ignoring
      e.printStackTrace();
    }
    System.out.println("Email sent!");
  }
}
