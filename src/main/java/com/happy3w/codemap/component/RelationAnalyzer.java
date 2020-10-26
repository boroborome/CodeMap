package com.happy3w.codemap.component;

import com.happy3w.codemap.model.ClassRelation;
import com.happy3w.codemap.utils.ConstConfig;
import com.happy3w.codemap.insn.InsnAnalyzerManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class RelationAnalyzer {
    @Autowired
    private SignatureAnalyzer signatureAnalyzer;

    public Stream<ClassRelation> collectRelations(ClassReader classReader) {
        ClassNode node = new ClassNode();
        classReader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        String sourceName = calculateSourceName(node.name);
        boolean isInnerClass = !sourceName.equals(node.name);
        String inheritRelation = isInnerClass ? ClassRelation.REFERENCE : ClassRelation.INHERIT;
        return Stream.of(
                Stream.of(new ClassRelation(sourceName, null, null)),
                toRelation(sourceName, inheritRelation , superClassStream(node)),
                toRelation(sourceName, inheritRelation, interfaceStream(node.interfaces)),
                toRelation(sourceName,
                        isInnerClass ? ClassRelation.REFERENCE: ClassRelation.MEMBER,
                        fieldStream(node.fields)),
                toRelation(sourceName, ClassRelation.REFERENCE, referenceStream(node))
        ).flatMap(Function.identity())
                .filter(r -> !(ClassRelation.REFERENCE.equals(r.getRelation()) && Objects.equals(r.getClassA(), r.getClassB())))
                .distinct();
    }

    private String calculateSourceName(String name) {
        if (!ConstConfig.MergeAnonymityClass) {
            return name;
        }

        int startPos = name.lastIndexOf('/');
        if (startPos < 0) {
            startPos = 0;
        }
        int endPos = name.indexOf('$', startPos);
        if (endPos < 0) {
            return name;
        }
        return name.substring(0, endPos);
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
                .flatMap(InsnAnalyzerManager::analyzeRefTypeDesc);
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
