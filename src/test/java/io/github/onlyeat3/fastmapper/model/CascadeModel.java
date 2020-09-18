package io.github.onlyeat3.fastmapper.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CascadeModel {
    private String name;
    private CascadeModel child;
    private List<CascadeModel> children = new ArrayList<>();
}
