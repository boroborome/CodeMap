package com.happy3w.codemap.component;

import com.happy3w.codemap.model.ClassRelation;
import com.happy3w.codemap.strategy.insn.InsnAnalyzerManager;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class RelationAnalyzer {
    private final SignatureAnalyzer signatureAnalyzer;
    private final InsnAnalyzerManager insnAnalyzerManager;
    private final InnerClassAdapter innerClassAdapter;

    public RelationAnalyzer(
            SignatureAnalyzer signatureAnalyzer,
            InsnAnalyzerManager insnAnalyzerManager,
            InnerClassAdapter innerClassAdapter) {
        this.signatureAnalyzer = signatureAnalyzer;
        this.insnAnalyzerManager = insnAnalyzerManager;
        this.innerClassAdapter = innerClassAdapter;
    }

    public Stream<ClassRelation> collectRelations(ClassNode node) {
        return Stream.of(
                Stream.of(new ClassRelation(node.name, null, null)),
                toRelation(node.name, ClassRelation.INHERIT , superClassStream(node)),
                toRelation(node.name, ClassRelation.INHERIT, interfaceStream(node.interfaces)),
                toRelation(node.name, ClassRelation.MEMBER, fieldStream(node.fields)),
                toRelation(node.name, ClassRelation.REFERENCE, referenceStream(node))
        ).flatMap(Function.identity())
                .map(innerClassAdapter::adapt)
                .filter(r -> !(ClassRelation.REFERENCE.equals(r.getRelation()) && Objects.equals(r.getClassA(), r.getClassB())))
                .distinct();
    }

    private Stream<String> referenceStream(ClassNode node) {
        Stream<String> annotationTypeDescStream = node.visibleAnnotations == null
                ? Stream.empty()
                : node.visibleAnnotations.stream()
                        .map(annotation -> annotation.desc);
        Stream<String> methodTypeDescStream = node.methods.stream()
                .flatMap(method -> collectRelationFromMethod(method));
        return Stream.concat(annotationTypeDescStream, methodTypeDescStream);
    }

    private Stream<String> collectRelationFromMethod(MethodNode methodNode) {
        return Stream.of(
                signatureAnalyzer.analyzeTypes(methodNode.desc, methodNode.signature),
                collectRelationInInsn(methodNode.instructions)
        ).flatMap(Function.identity());

    }

    private Stream<String> collectRelationInInsn(InsnList instructions) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(instructions.iterator(), 0), false)
                .flatMap(insnAnalyzerManager::analyzeRefTypeDesc);
    }

    private Stream<String> fieldStream(List<FieldNode> fields) {
        if (fields == null || fields.isEmpty()) {
            return Stream.empty();
        }

        return fields.stream()
                .flatMap(fieldNode -> Stream.of(fieldNode.desc, fieldNode.signature));
    }

    private Stream<String> interfaceStream(List<String> interfaces) {
        return interfaces == null ? Stream.empty() : interfaces.stream();
    }

    private Stream<String> superClassStream(ClassNode node) {
        return Stream.of(node.superName, node.signature);
    }

    private Stream<ClassRelation> toRelation(String fromType, String relationType, Stream<String> toTypeStream) {
        return signatureAnalyzer.analyzeTypes(toTypeStream)
                .map(toType -> new ClassRelation(fromType, relationType, toType));
    }
}
