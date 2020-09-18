package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.math.BigInteger;

public class BigIntegerToLongTypeMapper implements TypeMapper<BigInteger, Long> {
    @Override
    public boolean match(MappingInfo<BigInteger, Long> mappingInfo) {
        return mappingInfo.getSourceType().getRawClass().equals(BigInteger.class) && mappingInfo.getTargetType().getRawClass().equals(Long.class);
    }

    @Override
    public Long map(MappingInfo<BigInteger, Long> mappingInfo) {
        return mappingInfo.getSource().longValue();
    }
}
