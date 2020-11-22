package com.happy3w.codemap.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ClassRelationFilter {
    private Set<String> includes = new HashSet<>();
    private Set<String> excludes = new HashSet<>();
    private Set<String> relationTypes = new HashSet<>();
    private int refCount = 2;

    private Set<String> fileRanges = new HashSet<>();
}
