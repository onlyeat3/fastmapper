package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.SimpleTypeMapper;

import java.sql.Timestamp;
import java.util.Date;

public class DateToTimeStampTypeMapper extends SimpleTypeMapper<Date, Timestamp> {
    @Override
    public boolean match(MappingInfo mappingInfo) {
        return Date.class.equals(mappingInfo.getSourceType().getRawClass()) && Timestamp.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public Timestamp map(MappingInfo<Date, Timestamp> mappingInfo) {
        return Timestamp.from(mappingInfo.getSource().toInstant());
    }
}
