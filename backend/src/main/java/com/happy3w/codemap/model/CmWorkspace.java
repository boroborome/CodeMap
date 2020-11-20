package com.happy3w.codemap.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CmWorkspace {
    private String id;
    private String name;
    private Set<String> includes = new HashSet<>();
    private Set<String> excludes = new HashSet<>();
    private Set<String> relationTypes = new HashSet<>();
    private Set<String> highlight = new HashSet<>();
    private Set<String> selected = new HashSet<>();
    private int refCount = 2;
    private boolean isDirty;

    private Set<String> fileToAnalyze = new HashSet<>();
    private Set<String> fileRanges = new HashSet<>();
}
