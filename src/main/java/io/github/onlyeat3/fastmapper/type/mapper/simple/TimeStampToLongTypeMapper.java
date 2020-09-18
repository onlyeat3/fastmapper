package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.sql.Timestamp;

public class TimeStampToLongTypeMapper implements TypeMapper<Timestamp, Long> {
    @Override
    public boolean match(MappingInfo<Timestamp, Long> mappingInfo) {
        return mappingInfo.getTargetType().getRawClass().equals(Long.class) && mappingInfo.getSourceType().getRawClass().equals(Timestamp.class);
    }

    @Override
    public Long map(MappingInfo<Timestamp, Long> mappingInfo) {
        return mappingInfo.getSource().getTime();
    }
}
