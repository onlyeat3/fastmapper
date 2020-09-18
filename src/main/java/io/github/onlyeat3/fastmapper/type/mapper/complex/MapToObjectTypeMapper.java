package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.FastMapper;
import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.ReflectionUtils;
import io.github.onlyeat3.fastmapper.type.ClassReference;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Map;

@Order(4000)
public class MapToObjectTypeMapper implements TypeMapper<Map<Object, Object>, Object> {
    @Override
    public boolean match(MappingInfo mappingInfo) {
        return mappingInfo.getSourceType().isMapLikeType() && !mappingInfo.getTargetType().isMapLikeType();
    }

    @Override
    public Object map(MappingInfo mappingInfo) {
        Object targetObj = mappingInfo.getTargetType().newInstance();
        //prepare
        Map<String, Object> cleanMap = FastMapper.map(mappingInfo.getSource(), new ClassReference<Map<String, Object>>() {
        });
        //convert
        Field[] targetPds = ReflectionUtils.getBeanInfo(mappingInfo.getTargetType().getRawClass());
        for (Field pd : targetPds) {
            Object value = cleanMap.get(pd.getName());
            if (value != null) {
                Object fieldValue = FastMapper.map(value, pd.getType());
                if (fieldValue != null) {
                    ReflectionUtils.setFieldValue(targetObj, fieldValue, pd);
                }
            }
        }
        return targetObj;
    }
}
