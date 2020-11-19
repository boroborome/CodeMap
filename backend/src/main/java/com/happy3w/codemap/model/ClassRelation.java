package com.happy3w.codemap.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.MessageFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassRelation {
    public static final String INHERIT = "inherit";
    public static final String MEMBER = "member";
    public static final String REFERENCE = "reference";
    public static final String OTHER = "other";

    private String id;

    @EqualsAndHashCode.Include
    private String classA;

    @EqualsAndHashCode.Include
    private String relation;

    @EqualsAndHashCode.Include
    private String classB;

    @EqualsAndHashCode.Include
    private String jarFile;

    public ClassRelation(String classA, String relation, String classB) {
        this.classA = classA;
        this.relation = relation;
        this.classB = classB;
    }

    @Override
    public String toString() {
        return String.format("%s-%s->%s", classA, relation, classB);
    }

    public void initId() {
        id = MessageFormat.format("{0}-{1}-{2}-{3}", jarFile, classA, relation, classB);
    }
}
