package com.alura.ecommerce;

import static com.alura.ecommerce.TopicConstants.ECOMMERCE_TOPIC;
import static com.alura.ecommerce.TopicConstants.ECOMMERCE_TOPIC_SEND_EMAIL;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    try (var orderDispatcher = new KafkaDispatcher<Order>()) {
      try (var mailDispatcher = new KafkaDispatcher<Email>()) {
        for (int i = 0; i < 10; i++) {
          var userId = UUID.randomUUID().toString();
          var orderId = UUID.randomUUID().toString();
          var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);
          var order = new Order(userId, orderId, amount);
          orderDispatcher.send(ECOMMERCE_TOPIC, userId, order);
          var email =
              new Email("Hello my dear", "Thank you for your order! We are processing your order!");
          mailDispatcher.send(ECOMMERCE_TOPIC_SEND_EMAIL, userId, email);
        }
      }
    }
  }
}
