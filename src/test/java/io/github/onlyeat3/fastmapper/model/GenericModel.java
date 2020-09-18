package io.github.onlyeat3.fastmapper.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class GenericModel<A, B, C> {
    private List<Map<A, B>> list = new ArrayList<>();
    private B b;
    private C c;
}
