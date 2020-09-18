package io.github.onlyeat3.fastmapper.type.mapper;

import io.github.onlyeat3.fastmapper.MappingInfo;

public interface TypeMapper<S, T> {
    boolean match(MappingInfo<S, T> mappingInfo);

    T map(MappingInfo<S, T> mappingInfo);

}
