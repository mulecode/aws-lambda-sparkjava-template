package uk.co.mulecode.lambda.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;

public final class JsonUtils {

  private static final ObjectMapper objectMapper = jacksonMapper();

  public static String toJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error processing object to json", e);
    }
  }

  public static <T> T toObject(String value, Class<T> type) {
    try {
      return objectMapper.readValue(value, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error processing string to Object: " + type.getName(), e);
    }
  }

  public static Map<String, String> toMap(String value) {
    try {
      return objectMapper.readValue(value, new TypeReference<Map<String, String>>() {
      });
    } catch (Exception e) {
      throw new RuntimeException("Error processing string to Map", e);
    }
  }

  private static ObjectMapper jacksonMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    return mapper;
  }
}
