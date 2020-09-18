package io.github.onlyeat3.fastmapper.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Fooo {
    private String stringField;
    private Timestamp timeStampField;
    private SomeEnum longField;
    private Bar bar;

    private List<Bar> bars = new ArrayList<>();
    private Bar[] barArray;

    private Map<Foo, Bar> fooBarMap = new HashMap<>();
}
