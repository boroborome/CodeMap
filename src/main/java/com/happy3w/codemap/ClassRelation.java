package com.happy3w.codemap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

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

    public static Stream<ClassRelation> relationStream(
            String fromType,
            String relationType,
            String... toTypes) {
        return relationStream(fromType, relationType, Stream.of(toTypes));
    }

    public static Stream<ClassRelation> relationStream(
            String fromType,
            String relationType,
            Stream<String> typeStream) {
        return typeStream
                .filter(Objects::nonNull)
                .flatMap(combineType -> SignatureAnalyzer.analyzeTypes(combineType).stream())
                .filter(typeName -> !isJavaPlantformType(typeName))
                .map(typeName -> new ClassRelation(fromType, relationType, typeName));
    }

    public static boolean isJavaPlantformType(String dataType) {
        return dataType.length() == 1
                || dataType.startsWith("java/");
    }
}
