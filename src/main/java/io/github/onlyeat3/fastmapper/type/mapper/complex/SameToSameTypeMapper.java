package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.FastMapper;
import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.JavaType;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.util.HashMap;
import java.util.Map;

@Order(1000)
public class SameToSameTypeMapper implements TypeMapper<Object, Object> {
    public static final Map<Class,Class> CLASS_ALIAS_MAP = new HashMap<>();
    static {
        CLASS_ALIAS_MAP.put(Byte.class,byte.class);
        CLASS_ALIAS_MAP.put(Short.class,short.class);
        CLASS_ALIAS_MAP.put(Integer.class,int.class);
        CLASS_ALIAS_MAP.put(Long.class,long.class);
        CLASS_ALIAS_MAP.put(Float.class,float.class);
        CLASS_ALIAS_MAP.put(Double.class,double.class);
        CLASS_ALIAS_MAP.put(Boolean.class,boolean.class);
        CLASS_ALIAS_MAP.put(Character.class,char.class);

        CLASS_ALIAS_MAP.put(byte.class,Byte.class);
        CLASS_ALIAS_MAP.put(short.class,Short.class);
        CLASS_ALIAS_MAP.put(int.class,Integer.class);
        CLASS_ALIAS_MAP.put(long.class,Long.class);
        CLASS_ALIAS_MAP.put(float.class,Float.class);
        CLASS_ALIAS_MAP.put(double.class,Double.class);
        CLASS_ALIAS_MAP.put(boolean.class,Boolean.class);
        CLASS_ALIAS_MAP.put(char.class,Character.class);
    }
    @Override
    public boolean match(MappingInfo<Object, Object> mappingInfo) {
        JavaType<Object> targetType = mappingInfo.getTargetType();
        JavaType<Object> sourceType = mappingInfo.getSourceType();
        if (sourceType.getRawClass() == targetType.getRawClass()) {
            return true;
        }
        Class aClass = CLASS_ALIAS_MAP.get(sourceType.getRawClass());
        if (aClass != null) {
            if (aClass.equals(targetType.getRawClass())) {
                return true;
            }
        }
        return targetType.isRootObject();
    }

    @Override
    public Object map(MappingInfo<Object, Object> mappingInfo) {
        if (mappingInfo.getSourceType().isEnum()) {
            return mappingInfo.getSource();
        }
        if (mappingInfo.getTargetType().isPrimitive()) {
            return mappingInfo.getSource();
        }
        if (mappingInfo.getTargetType().isFinal()) {
            return mappingInfo.getSource();
        }
        if (mappingInfo.getSourceType().getRawClass().equals(mappingInfo.getTargetType().getRawClass())) {
            if (mappingInfo.getSourceType().isWellKnownType()) {
                return mappingInfo.getSource();
            }
        }

        if (mappingInfo.getTargetType().isRootObject()) {
            if (!mappingInfo.getSourceType().isRootObject()) {
                return FastMapper.map(mappingInfo.getSource(), mappingInfo.getSourceType().getRawClass());
            }
            return null;
        }
        return null;
    }
}
