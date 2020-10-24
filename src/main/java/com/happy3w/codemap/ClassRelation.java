package com.happy3w.codemap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ClassRelation {
    public static final String INHERIT = "inherit";
    public static final String MEMBER = "member";
    public static final String REFERENCE = "reference";
    public static final String OTHER = "other";

    private String classA;
    private String relation;
    private String classB;
}
