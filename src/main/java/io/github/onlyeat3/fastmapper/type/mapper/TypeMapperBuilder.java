package io.github.onlyeat3.fastmapper.type.mapper;

import io.github.onlyeat3.fastmapper.MappingInfo;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Function;
import java.util.function.Predicate;

@Setter
@Accessors(fluent = true, chain = true)
public class TypeMapperBuilder<S, T> {
    private Predicate<MappingInfo<S, T>> matcher;
    private Function<S, T> mapFunction;

    public TypeMapper<S, T> build() {
        return new TypeMapper<S, T>() {
            @Override
            public boolean match(MappingInfo<S, T> mappingInfo) {
                return matcher.test(mappingInfo);
            }

            @Override
            public T map(MappingInfo<S, T> mappingInfo) {
                return mapFunction.apply(mappingInfo.getSource());
            }
        };
    }

    public static <R, T> TypeMapperBuilder<R, T> of() {
        return new TypeMapperBuilder<R, T>();
    }
}
