package com.happy3w.codemap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class RelationAnalyzer {
    public Stream<ClassRelation> collectRelations(ClassReader classReader) {
        ClassNode node = new ClassNode();
        classReader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return Stream.of(
                superClassStream(ClassRelation.INHERIT, node),
                interfaceStream(ClassRelation.INHERIT, node, node.interfaces),
                fieldStream(ClassRelation.MEMBER, node, node.fields),
                referenceStream(ClassRelation.REFERENCE, node, node.methods)
        ).flatMap(Function.identity())
                .distinct();
    }

    private Stream<ClassRelation> referenceStream(String relationType, ClassNode node, List<MethodNode> methods) {
        return Stream.empty();
    }

    private Stream<ClassRelation> fieldStream(String relationType, ClassNode node, List<FieldNode> fields) {
        if (fields == null || fields.isEmpty()) {
            return Stream.empty();
        }

        return fields.stream()
                .flatMap(fieldNode -> Stream.of(fieldNode.desc, fieldNode.signature))
                .filter(Objects::nonNull)
                .flatMap(combineType -> SignatureAnalyzer.analyzeTypes(combineType).stream())
                .filter(typeName -> !isJavaPlantformType(typeName))
                .map(typeName -> new ClassRelation(node.name, relationType, typeName));
    }

    private Stream<ClassRelation> interfaceStream(String relationType, ClassNode node, List<String> interfaces) {
        if (interfaces == null || interfaces.isEmpty()) {
            return Stream.empty();
        }

        return interfaces.stream()
                .flatMap(className -> relationStream(node.name, relationType, className));
    }

    private Stream<ClassRelation> superClassStream(String relationType, ClassNode node) {
        return relationStream(node.name, relationType, node.superName, node.signature);
    }

    private Stream<ClassRelation> relationStream(
            String fromType,
            String relationType,
            String... toTypes) {
        return Stream.of(toTypes)
                .filter(Objects::nonNull)
                .flatMap(combineType -> SignatureAnalyzer.analyzeTypes(combineType).stream())
                .filter(typeName -> !isJavaPlantformType(typeName))
                .map(typeName -> new ClassRelation(fromType, relationType, typeName));
    }

    private boolean isJavaPlantformType(String dataType) {
        return dataType.length() == 1
                || dataType.startsWith("java/");
    }
}
