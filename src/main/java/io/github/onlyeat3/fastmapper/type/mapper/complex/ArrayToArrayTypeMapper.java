package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.FastMapper;
import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.util.Arrays;

@Order(6000)
public class ArrayToArrayTypeMapper implements TypeMapper<Object[], Object[]> {
    @Override
    public boolean match(MappingInfo<Object[], Object[]> mappingInfo) {
        return mappingInfo.getSourceType().isArray() && mappingInfo.getTargetType().isArray();
    }

    @Override
    public Object[] map(MappingInfo<Object[], Object[]> mappingInfo) {
        if (mappingInfo.getSource() == null) {
            return null;
        }
        Class<?> targetArrayComponentType = mappingInfo.getTargetType().getRawClass().getComponentType();
        Object[] targetArray = mappingInfo.getTargetType().newInstance();
        if (mappingInfo.getSource().length == 0) {
            return targetArray;
        }
        targetArray = Arrays.copyOf(targetArray, mappingInfo.getSource().length);
        for (int i = 0; i < mappingInfo.getSource().length; i++) {
            Object s = mappingInfo.getSource()[i];
            Object arrayElement = FastMapper.map(s, targetArrayComponentType);
            targetArray[i] = arrayElement;
        }
        return targetArray;
    }
}
