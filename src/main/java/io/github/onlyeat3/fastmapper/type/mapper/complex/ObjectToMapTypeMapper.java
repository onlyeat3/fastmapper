package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.ReflectionUtils;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Order(2000)
public class ObjectToMapTypeMapper implements TypeMapper<Object,Map<String, Object>> {

    @Override
    public boolean match(MappingInfo<Object, Map<String, Object>> mappingInfo) {
        return !mappingInfo.getSourceType().isMapLikeType() && mappingInfo.getTargetType().isMapLikeType();
    }

    @Override
    public Map<String, Object> map(MappingInfo<Object, Map<String, Object>> mappingInfo) {
        Map<String, Object> targetMap = new HashMap<>();
        //convert
        Field[] sourcePds = ReflectionUtils.getBeanInfo(mappingInfo.getSourceType().getRawClass());
        for (Field sourcePd : sourcePds) {
            Object targetPropertyValue = ReflectionUtils.getFieldValue(mappingInfo.getSource(), sourcePd.getName());
            if (targetPropertyValue != null) {
                targetMap.put(sourcePd.getName(),targetPropertyValue);
            }
        }
        return targetMap;
    }
}
