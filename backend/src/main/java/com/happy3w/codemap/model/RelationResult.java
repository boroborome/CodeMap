package com.happy3w.codemap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RelationResult {
    private List<ClassInfo> nodes;
    private List<ClassRelation> relations;
    private boolean limitedResult;
}
