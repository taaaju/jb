package oxygen.services.jobs_processor.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Utility {
    private Utility() {
    }

    public static final ObjectMapper defaultMapper = mapper(null);
    public static final ObjectMapper snakeCasedMapper = mapper(PropertyNamingStrategies.SNAKE_CASE);

    private static ObjectMapper mapper(PropertyNamingStrategy propertyNamingStrategy) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (propertyNamingStrategy != null) {
            mapper.setPropertyNamingStrategy(propertyNamingStrategy);
        }
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        return mapper;
    }


    public static String asStringOrEmpty(Object o) {
        if (Objects.nonNull(o)) {
            if (o instanceof String s) {
                return s;
            }
            return o.toString();
        }
        return "";
    }

    public static String asJsonString(Object o) {
        return asJsonString(o, defaultMapper);
    }

    public static String asSnakeCasedJsonString(Object o) {
        return asJsonString(o, snakeCasedMapper);
    }

    public static String asJsonString(Object o, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            log.error("failed to convert to json string {} {}", o, e.getMessage());
        }
        return "";
    }

    public static Map<String, Object> toMapSafely(Object o) {
        if (Objects.nonNull(o)) {
            if (o instanceof Map<?, ?> m) {
                return (Map<String, Object>) m;
            }

            if (o instanceof String s) {
                return readStringAsMap(s);
            }
        }

        return new HashMap<>();
    }

    public static Map<String, Object> readStringAsMap(String s) {
        if (Objects.nonNull(s)) {
            try {
                return snakeCasedMapper.readValue(s, new TypeReference<Map<String, Object>>() {});
            } catch (Exception ignored) {}
        }
        return new HashMap<>();
    }
}
