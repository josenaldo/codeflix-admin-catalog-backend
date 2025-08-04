package br.com.josenaldo.codeflix.catalog.infrastructure.configuration.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import java.util.concurrent.Callable;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public enum Json {
    INSTANCE;

    private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
        .dateFormat(new StdDateFormat())
        .featuresToDisable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
            DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
        .modules(
            new JavaTimeModule(),
            new Jdk8Module(),
            afterBurnerModule()
        )
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .build();

    public static ObjectMapper getMapper() {
        return INSTANCE.mapper.copy();
    }

    public static String writeValueAsString(final Object obj) {
        return invoke(() -> INSTANCE.mapper.writeValueAsString(obj));
    }

    public static <T> T readValue(final String json, final Class<T> clazz) {
        return invoke(() -> INSTANCE.mapper.readValue(json, clazz));
    }



    /**
     * Make Afterburner generates bytecode only for public getters/setters and fields.
     * Without this, Java 9+ complains of "Illegal reflexive access"
     *
     */
    private Module afterBurnerModule() {
        var module = new AfterburnerModule();
        module.setUseValueClassLoader(false);
        return module;
    }

    private static <T> T invoke(Callable<T> callable) {
        try {
            return callable.call();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
