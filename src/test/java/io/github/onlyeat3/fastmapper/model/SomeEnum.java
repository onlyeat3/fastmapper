package io.github.onlyeat3.fastmapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SomeEnum {
    ONE(1, "1"),
    TWO(2, "2"),
    ;

    private Integer value;
    private String remark;

    public static SomeEnum valueOf(Integer value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
