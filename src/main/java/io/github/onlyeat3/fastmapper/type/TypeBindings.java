package io.github.onlyeat3.fastmapper.type;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeBindings {
    @Setter
    @Getter
    private List<String> names = new ArrayList<>();

    @Getter
    @Setter
    private List<Type> values = new ArrayList<>();

    public static TypeBindings empty() {
        return new TypeBindings();
    }

    public Type put(String key, Type value) {
        names.add(key);
        values.add(value);
        return value;
    }

    public void merge(TypeBindings typeBindings) {
        List<String> newNames = typeBindings.getNames();
        List<Type> newValues = typeBindings.getValues();
        if (newNames.size() == newValues.size()) {
            this.names.addAll(newNames);
            this.values.addAll(newValues);
        }
    }

    public Type firstValue() {
        if (this.getValues().isEmpty()) {
            return null;
        }
        return this.getValues().get(0);
    }
}
