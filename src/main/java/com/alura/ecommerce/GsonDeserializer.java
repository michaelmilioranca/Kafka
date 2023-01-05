package com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer<T> implements Deserializer<T> {
  public static final String TYPE_CONFIG = "TYPE_CONFIG";
  private final Gson gson = new GsonBuilder().create();
  private Class<T> type;

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    var typeName = String.valueOf(configs.get(TYPE_CONFIG));
    try {
      this.type = (Class<T>) Class.forName(typeName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(
          "Type for the deserialization does not exists in the classpath", e);
    }
  }

  @Override
  public T deserialize(String topic, byte[] data) {
    return gson.fromJson(new String(data), type);
  }
}
