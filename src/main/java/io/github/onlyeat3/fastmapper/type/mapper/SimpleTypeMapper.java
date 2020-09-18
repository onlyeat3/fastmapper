package io.github.onlyeat3.fastmapper.type.mapper;

import io.github.onlyeat3.fastmapper.MappingInfo;
import lombok.Setter;

public abstract class SimpleTypeMapper<S,T> implements TypeMapper<S,T> {
    @Setter
    protected Class<S> sClass;
    @Setter
    protected Class<T> tClass;
    @Override
    public boolean match(MappingInfo mappingInfo) {
        boolean match = (mappingInfo.getSourceType().getRawClass().equals(this.sClass) && mappingInfo.getTargetType().getRawClass().equals(this.tClass))
                || (mappingInfo.getSourceType().getRawClass().equals(this.tClass) && mappingInfo.getTargetType().getRawClass().equals(this.sClass));
        return match;
    }
}
