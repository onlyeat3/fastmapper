package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.sql.Timestamp;

public class StringToTimestampTypeMapper implements TypeMapper<String, Timestamp> {

    @Override
    public boolean match(MappingInfo<String, Timestamp> mappingInfo) {
        return String.class.equals(mappingInfo.getSourceType().getRawClass()) && Timestamp.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public Timestamp map(MappingInfo<String, Timestamp> mappingInfo) {
        return Timestamp.valueOf(mappingInfo.getSource());
    }
}
