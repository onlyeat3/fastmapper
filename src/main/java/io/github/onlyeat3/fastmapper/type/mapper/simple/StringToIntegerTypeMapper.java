package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

public class StringToIntegerTypeMapper implements TypeMapper<String, Integer> {

    @Override
    public boolean match(MappingInfo<String, Integer> mappingInfo) {
        return String.class.equals(mappingInfo.getSourceType().getRawClass()) && Integer.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public Integer map(MappingInfo<String, Integer> mappingInfo) {
        return Integer.parseInt(mappingInfo.getSource());
    }
}
