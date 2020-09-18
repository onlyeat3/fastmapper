package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.FastMapper;
import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.ReflectionUtils;
import io.github.onlyeat3.fastmapper.type.JavaType;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@Order(Integer.MAX_VALUE)
public class ObjectToObjectTypeMapper implements TypeMapper<Object, Object> {
    @Override
    public boolean match(MappingInfo<Object, Object> mappingInfo) {
        return true;
    }

    @Override
    public Object map(MappingInfo<Object, Object> mappingInfo) {
        JavaType<Object> targetType = mappingInfo.getTargetType();
        Object targetObj = targetType.newInstance();
        if (targetObj == null) {
            return null;
        }
        try {
            Field[] fields = ReflectionUtils.getBeanInfo(targetType.getRawClass());
            for (Field targetPd : fields) {
                Object sourceValue = ReflectionUtils.getFieldValue(mappingInfo.getSource(), targetPd.getName());
                if (sourceValue == null) {
                    continue;
                }
                Type fieldClass = ReflectionUtils.getFieldClass(targetObj, targetPd.getName());
                if (fieldClass == null) {
                    fieldClass = targetPd.getType();
                }
                Object targetValue = FastMapper.map(sourceValue,fieldClass);
                if (targetValue == null) {
                    continue;
                }
                ReflectionUtils.setFieldValue(targetObj, targetValue, targetPd);
            }
            return targetObj;
        } catch (Exception ignored) {
        }
        return null;
    }
}
