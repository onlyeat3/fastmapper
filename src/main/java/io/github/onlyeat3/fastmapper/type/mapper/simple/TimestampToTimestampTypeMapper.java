package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.sql.Timestamp;

public class TimestampToTimestampTypeMapper implements TypeMapper<Timestamp, Timestamp> {

    @Override
    public boolean match(MappingInfo<Timestamp, Timestamp> mappingInfo) {
        return Timestamp.class.equals(mappingInfo.getSourceType().getRawClass()) && Timestamp.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public Timestamp map(MappingInfo<Timestamp, Timestamp> mappingInfo) {
        return Timestamp.from(mappingInfo.getSource().toInstant());
    }

}
