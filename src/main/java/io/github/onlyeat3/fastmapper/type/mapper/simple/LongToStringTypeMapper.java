package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

public class LongToStringTypeMapper implements TypeMapper<Long, String> {
    @Override
    public boolean match(MappingInfo<Long, String> mappingInfo) {
        return mappingInfo.getSourceType().getRawClass().equals(Long.class) && mappingInfo.getTargetType().getRawClass().equals(String.class);
    }

    @Override
    public String map(MappingInfo<Long, String> mappingInfo) {
        return String.valueOf(mappingInfo.getSource());
    }
}
