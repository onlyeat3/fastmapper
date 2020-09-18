package io.github.onlyeat3.fastmapper;

import io.github.onlyeat3.fastmapper.type.JavaType;
import lombok.Getter;

public class MappingInfo<S, T> {
    @Getter
    private S source;
    private Class<T> targetClass;
    @Getter
    private JavaType<S> sourceType;
    @Getter
    private JavaType<T> targetType;
    private T targetObj;

    public static <S, T> MappingInfo of(S source, JavaType<T> targetType, T targetObj) {
        MappingInfo<S, T> mappingInfo = new MappingInfo<>();
        mappingInfo.source = source;
        mappingInfo.targetClass = targetType.getRawClass();
        mappingInfo.sourceType = JavaType.from(mappingInfo.source.getClass());
        mappingInfo.targetType = targetType;
        mappingInfo.targetType.setInstance(targetObj);
        mappingInfo.targetObj = targetObj;
        return mappingInfo;
    }

    public static <S, T> MappingInfo of(S source, JavaType<T> targetType) {
        return of(source, targetType, null);
    }

    public static <S, T> MappingInfo of(S source, Class<T> targetClass) {
        return of(source, JavaType.from(targetClass), null);
    }


}
