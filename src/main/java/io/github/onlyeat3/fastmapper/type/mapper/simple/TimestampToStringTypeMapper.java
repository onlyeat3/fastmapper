package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.sql.Timestamp;

public class TimestampToStringTypeMapper implements TypeMapper<Timestamp, String> {

    @Override
    public boolean match(MappingInfo<Timestamp, String> mappingInfo) {
        return Timestamp.class.equals(mappingInfo.getSourceType().getRawClass()) && String.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public String map(MappingInfo<Timestamp, String> mappingInfo) {
        return mappingInfo.getSource().toString();
    }

}
