package io.github.onlyeat3.fastmapper.type;

import io.github.onlyeat3.fastmapper.ReflectionUtils;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ClassReference<T> {
    @Getter
    protected String fieldName;
    protected final Type _type;

    protected ClassReference(String fieldName) {
        this();
        this.fieldName = fieldName;
    }

    protected ClassReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        } else {
            this._type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this._type;
    }

    public Class<?> getTypeClass() {
        return ReflectionUtils.loadClass(this.getType().getTypeName());
    }
}
