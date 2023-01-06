package com.alura.ecommerce;

import static com.alura.ecommerce.TopicConstants.ECOMMERCE_TOPIC_SEND_EMAIL;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

  public static void main(String[] args) {
    var emailService = new EmailService();
    try (var kafkaService =
        KafkaServiceCreator.groupId(EmailService.class.getSimpleName())
            .topic(ECOMMERCE_TOPIC_SEND_EMAIL)
            .parse(emailService::parse)
            .type(Email.class)
            .create()) {
      kafkaService.run();
    }
  }

  private void parse(ConsumerRecord<String, Email> record) {
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
