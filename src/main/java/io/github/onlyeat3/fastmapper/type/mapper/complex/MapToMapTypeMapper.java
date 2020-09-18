package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.FastMapper;
import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(4000)
public class MapToMapTypeMapper implements TypeMapper<Map<Object, Object>, Map<Object, Object>> {
    @Override
    public boolean match(MappingInfo<Map<Object, Object>, Map<Object, Object>> mappingInfo) {
        return Map.class.isAssignableFrom(mappingInfo.getSourceType().getRawClass()) && Map.class.isAssignableFrom(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public Map<Object, Object> map(MappingInfo<Map<Object, Object>, Map<Object, Object>> mappingInfo) {
        Map<Object, Object> resultMap = new HashMap<>();
        List<Type> actualArguments = mappingInfo.getTargetType().getTypeBindings().getValues();
        for (Map.Entry<Object, Object> entry : mappingInfo.getSource().entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            if (entry.getValue() == null) {
                continue;
            }
            if (!actualArguments.isEmpty() && actualArguments.size() >= 2) {
                Object targetKey = FastMapper.map(entry.getKey(), actualArguments.get(0));
                Object targetValue = FastMapper.map(entry.getValue(), actualArguments.get(1));
                if (targetKey != null && targetValue != null) {
                    resultMap.put(targetKey, targetValue);
                }
            }
        }
        return resultMap;
    }
}
