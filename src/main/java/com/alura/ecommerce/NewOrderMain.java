package com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_TOPIC;
import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_TOPIC_SEND_EMAIL;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(getKafkaProperties());
        String key = UUID.randomUUID().toString();
        String message = key.concat(",5312156,3321.53");
        // For this simple test we will be using the same value for key and value
        ProducerRecord<String, String> record = new ProducerRecord<>(ECOMMERCE_TOPIC, key, message);
        // The send is assyncronous, if we want to something with the "return" of it, we have to use get
        // The send also can receive a callback, which is has de data and the exception
        producer.send(record, getDefaultCallback()).get();
        var email = "Thank you for your order! We are processing your order!";
        var emailRecord = new ProducerRecord<>(ECOMMERCE_TOPIC_SEND_EMAIL, key, email);
        producer.send(emailRecord,getDefaultCallback()).get();
    }

    private static Callback getDefaultCallback() {
        return (data, ex) -> {
            if (Objects.nonNull(ex)) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Message sent to " + data.topic() + ":::partition" + data.partition() + "/.offseet" + data.offset() + "/.timestamp" + data.timestamp());
        };
    }

    private static Properties getKafkaProperties() {
        Properties properties = new Properties();
        // Where the kafka is running
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // The class to serialize the KEY value of the message
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // The class to serialize the VALUE of the message
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
