package io.github.onlyeat3.fastmapper.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Foo {
    private String stringField;
    private Timestamp timeStampField;
    private Long longField;
}
