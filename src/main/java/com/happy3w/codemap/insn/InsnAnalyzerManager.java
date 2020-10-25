package com.happy3w.codemap.insn;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class InsnAnalyzerManager {
    private static final Map<Class, InsnAnalyzer> insnAnalyzerMap = new HashMap<>();
    private static Set<Class> typeSets = new HashSet<>();

    public static <T> void regAnalyzer(InsnAnalyzer<T> analyzer) {
        insnAnalyzerMap.put(analyzer.getInsnNodeType(), analyzer);
    }

    static {
        regAnalyzer(new MethodInsnAnalyzer());
        regAnalyzer(new FieldInsnNodeAnalyzer());
        regAnalyzer(new InvokeDynamicInsnNodeAnalyzer());
        regAnalyzer(new TypeInsnNodeAnalyzer());
    }

    public static Stream<String> analyzeRefTypeDesc(AbstractInsnNode insnNode) {
        InsnAnalyzer analyzer = insnAnalyzerMap.get(insnNode.getClass());
        if (analyzer == null) {
            if (!typeSets.contains(insnNode.getClass())) {
                System.out.println("Unknown insn:" + insnNode.getClass());
                typeSets.add(insnNode.getClass());
            }
            return Stream.empty();
        }

        return analyzer.refTypes(insnNode);
    }

}
