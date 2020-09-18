package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.SimpleTypeMapper;

import java.sql.Timestamp;
import java.util.Date;

public class TimeStampToDateTypeMapper extends SimpleTypeMapper<Timestamp,Date> {
    @Override
    public boolean match(MappingInfo mappingInfo) {
        return Timestamp.class.equals(mappingInfo.getSourceType().getRawClass()) && Date.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public Date map(MappingInfo<Timestamp, Date> mappingInfo) {
        return Date.from(mappingInfo.getSource().toInstant());
    }
}
