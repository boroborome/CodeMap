package com.happy3w.codemap;

import com.happy3w.codemap.insn.InsnAnalyzerManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RelationAnalyzer {
    public Stream<ClassRelation> collectRelations(ClassReader classReader) {
        ClassNode node = new ClassNode();
        classReader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return Stream.of(
                Stream.of(new ClassRelation(node.name, null, null)),
                superClassStream(ClassRelation.INHERIT, node),
                interfaceStream(ClassRelation.INHERIT, node, node.interfaces),
                fieldStream(ClassRelation.MEMBER, node, node.fields),
                referenceStream(ClassRelation.REFERENCE, node, node.methods)
        ).flatMap(Function.identity())
                .filter(r -> !(ClassRelation.REFERENCE.equals(r.getRelation()) && Objects.equals(r.getClassA(), r.getClassB())))
                .distinct();
    }

    private Stream<ClassRelation> referenceStream(String relationType, ClassNode node, List<MethodNode> methods) {
        Stream<String> typeDescStream = methods.stream()
                .flatMap(method -> collectRelationFromMethod(method));
        return SignatureAnalyzer.analyzeTypes(typeDescStream)
                .map(toType -> new ClassRelation(node.name, relationType, toType));
    }

    private Stream<String> collectRelationFromMethod(MethodNode methodNode) {
        return Stream.of(
                SignatureAnalyzer.analyzeTypes(methodNode.desc, methodNode.signature),
                collectRelationInInsn(methodNode.instructions)
        ).flatMap(Function.identity());

    }

    private Stream<String> collectRelationInInsn(InsnList instructions) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(instructions.iterator(), 0), false)
                .flatMap(InsnAnalyzerManager::analyzeRefTypeDesc);
    }

    private Stream<ClassRelation> fieldStream(String relationType, ClassNode node, List<FieldNode> fields) {
        if (fields == null || fields.isEmpty()) {
            return Stream.empty();
        }

        Stream<String> fieldTypeDescStream = fields.stream()
                .flatMap(fieldNode -> Stream.of(fieldNode.desc, fieldNode.signature));
        return SignatureAnalyzer.analyzeTypes(fieldTypeDescStream)
                .map(toType -> new ClassRelation(node.name, relationType, toType));
    }

    private Stream<ClassRelation> interfaceStream(String relationType, ClassNode node, List<String> interfaces) {
        if (interfaces == null || interfaces.isEmpty()) {
            return Stream.empty();
        }

        return SignatureAnalyzer.analyzeTypes(interfaces.stream())
                .map(toType -> new ClassRelation(node.name, relationType, toType));
    }

    private Stream<ClassRelation> superClassStream(String relationType, ClassNode node) {
        Stream<String> typeDescStream = Stream.concat(
                node.visibleAnnotations == null ? Stream.empty() : node.visibleAnnotations.stream()
                        .map(annotation -> annotation.desc),
                Stream.of(node.superName, node.signature));
        return SignatureAnalyzer.analyzeTypes(typeDescStream)
                .map(toType -> new ClassRelation(node.name, relationType, toType));
    }
}
