package com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.alura.ecommerce.util.PublicConstants.ECOMMERCE_TOPIC;

public class FraudDetectorService {

    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(getConsumerProperties());
        // The consumer can subscribe to a list of topics, but its not recommend because it would be a MESS
        consumer.subscribe(Collections.singletonList(ECOMMERCE_TOPIC));
        // Just to keep the consumer listening to the topic :)
        while(true){
            // The poll will return a list of records given the duration of the "wait"
            var records = consumer.poll(Duration.ofMillis(500));
            if(!records.isEmpty()){
             for(var record: records){
                 System.out.println("---------------------------------------");
                 System.out.println("Processing new order, checking fraud...");
                 System.out.println(record.key());
                 System.out.println(record.value());
                 System.out.println(record.partition());
                 System.out.println(record.offset());
             }
            }
        }
    }

    private static Properties getConsumerProperties() {
        var properties = new Properties();
        // Where the kafka is running
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // Class used to DESERIALIZE the key
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Class used to DESERIALIZE the value
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // The group ID is neeeded and not often repeated between projects
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
        return properties;
    }
}
