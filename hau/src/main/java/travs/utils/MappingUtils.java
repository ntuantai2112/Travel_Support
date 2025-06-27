package travs.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class MappingUtils {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <T> T map(Object source, Class<T> clazz) {
        if (source == null) return null;
        return mapper.map(source, clazz);
    }

    public static <T> List<T> map(List<? extends Object> sources, Class<T> clazz) {
        if (sources == null) return Collections.emptyList();
        return sources.stream()
                .map(item -> MappingUtils.map(item, clazz))
                .collect(Collectors.toList());
    }

    public static <T> T json2Obj(String json, Class<T> tClass) {
        if (json == null) return null;
        try {
            return new ObjectMapper().readValue(json, tClass);
        } catch (IOException e) {
            log.error("Err parse object from Json to Object");
            return null;
        }
    }

    public static <T> T map(Map<?, ?> mapValue, Class<T> tClass) {
        if (mapValue == null) return null;
        try {
            return new ObjectMapper().convertValue(mapValue, tClass);
        } catch (Exception e) {
            log.error("Err parse object from Map to Object");
            return null;
        }
    }

    public static Double getValueAsDouble(String value) {
        return value == null ? 0 : Double.valueOf(value);
    }
}
