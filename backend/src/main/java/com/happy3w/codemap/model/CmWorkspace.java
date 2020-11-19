package com.happy3w.codemap.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CmWorkspace {
    private String id;
    private String name;
    private List<String> includes = new ArrayList<>();
    private List<String> excludes = new ArrayList<>();
    private List<String> relationTypes = new ArrayList<>();
    private List<String> selected = new ArrayList<>();
    private int refCount = 2;
    private boolean isDirty;

    private List<String> fileToAnalyze = new ArrayList<>();
    private List<String> fileRanges = new ArrayList<>();
}
