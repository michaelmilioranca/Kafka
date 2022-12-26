package com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

// This class is a generic to serializer anything when we call send from the KafkaDispatcher
public class GsonSerializer<T> implements Serializer<T> {
  private final Gson gson = new GsonBuilder().create();

  @Override
  public byte[] serialize(String topic, T data) {
    return gson.toJson(data).getBytes();
  }
}
