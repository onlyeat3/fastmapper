package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.sql.Timestamp;

public class LongToTimeStampTypeMapper implements TypeMapper<Long, Timestamp> {
    @Override
    public boolean match(MappingInfo<Long, Timestamp> mappingInfo) {
        return mappingInfo.getSourceType().getRawClass().equals(Long.class) && mappingInfo.getTargetType().getRawClass().equals(Timestamp.class);
    }

    @Override
    public Timestamp map(MappingInfo<Long, Timestamp> mappingInfo) {
        return new Timestamp(mappingInfo.getSource());
    }
}
