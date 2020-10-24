package com.happy3w.codemap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class RelationAnalyzer {
    public Stream<ClassRelation> collectRelations(ClassReader classReader) {
        ClassNode node = new ClassNode();
        classReader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return Stream.of(
                superClassStream(ClassRelation.INHERIT, node, node.superName),
                interfaceStream(ClassRelation.INHERIT, node, node.interfaces),
                fieldStream(ClassRelation.MEMBER, node, node.fields),
                referenceStream(ClassRelation.REFERENCE, node, node.methods)
        ).flatMap(Function.identity());
    }

    private Stream<ClassRelation> referenceStream(String relationType, ClassNode node, List<MethodNode> methods) {
        return Stream.empty();
    }

    private Stream<ClassRelation> fieldStream(String relationType, ClassNode node, List<FieldNode> fields) {
        if (fields == null || fields.isEmpty()) {
            return Stream.empty();
        }

        return fields.stream()
                .filter(fieldNode -> !isJavaPlantformType(fieldNode.desc))
                .map(fieldNode -> new ClassRelation(node.name, relationType, fieldNode.desc));
    }

    private Stream<ClassRelation> interfaceStream(String relationType, ClassNode node, List<String> interfaces) {
        if (interfaces == null || interfaces.isEmpty()) {
            return Stream.empty();
        }

        return interfaces.stream()
                .filter(className -> !isJavaPlantformType(className))
                .map(className -> new ClassRelation(node.name, relationType, className));
    }

    private Stream<ClassRelation> superClassStream(String relationType, ClassNode node, String className) {
        if (className == null || isJavaPlantformType(className)) {
            return Stream.empty();
        }

        return Stream.of(new ClassRelation(node.name, relationType, className));
    }

    private boolean isJavaPlantformType(String dataType) {
        return dataType.length() <= 2
                || dataType.startsWith("java/")
                || dataType.startsWith("Ljava/");
    }
}
