package io.github.onlyeat3.fastmapper.type.mapper.simple;

import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.mapper.SimpleTypeMapper;

import java.math.BigDecimal;

public class BigDecimalToBigDecimalTypeMapper extends SimpleTypeMapper<BigDecimal, BigDecimal> {

    @Override
    public boolean match(MappingInfo mappingInfo) {
        return BigDecimal.class.equals(mappingInfo.getSourceType().getRawClass()) && BigDecimal.class.equals(mappingInfo.getTargetType().getRawClass());
    }

    @Override
    public BigDecimal map(MappingInfo<BigDecimal, BigDecimal> mappingInfo) {
        return BigDecimal.valueOf(mappingInfo.getSource().doubleValue());
    }
}
