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
        return methods.stream()
                .flatMap(method -> collectRelationFromMethod(method, relationType, node));
    }

    private Stream<ClassRelation> collectRelationFromMethod(MethodNode methodNode, String relationType, ClassNode classNode) {
        return Stream.of(
                ClassRelation.relationStream(classNode.name, relationType, methodNode.desc, methodNode.signature),
                collectRelationInInsn(methodNode.instructions, relationType, classNode)
        ).flatMap(Function.identity());
    }

    private Stream<ClassRelation> collectRelationInInsn(InsnList instructions, String relationType, ClassNode classNode) {
        Stream<String> typeStream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(instructions.iterator(), 0), false)
                .flatMap(InsnAnalyzerManager::analyzeRefTypeDesc);
        return ClassRelation.relationStream(classNode.name, relationType, typeStream);
    }

    private Stream<ClassRelation> fieldStream(String relationType, ClassNode node, List<FieldNode> fields) {
        if (fields == null || fields.isEmpty()) {
            return Stream.empty();
        }

        Stream<String> fieldTypeStream = fields.stream()
                .flatMap(fieldNode -> Stream.of(fieldNode.desc, fieldNode.signature));
        return ClassRelation.relationStream(node.name, relationType, fieldTypeStream);
    }

    private Stream<ClassRelation> interfaceStream(String relationType, ClassNode node, List<String> interfaces) {
        if (interfaces == null || interfaces.isEmpty()) {
            return Stream.empty();
        }

        return interfaces.stream()
                .flatMap(className -> ClassRelation.relationStream(node.name, relationType, className));
    }

    private Stream<ClassRelation> superClassStream(String relationType, ClassNode node) {
        return ClassRelation.relationStream(node.name, relationType, node.superName, node.signature);
    }


}
