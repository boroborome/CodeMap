package com.happy3w.codemap.strategy.insn;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class InsnAnalyzerManager {
    private Map<Class, InsnAnalyzer> insnAnalyzerMap = new HashMap<>();
    private Set<Class> typeSets = new HashSet<>();

    public InsnAnalyzerManager(List<InsnAnalyzer> analyzerList) {
        for (InsnAnalyzer analyzer : analyzerList) {
            insnAnalyzerMap.put(analyzer.getInsnNodeType(), analyzer);
        }
    }

    public Stream<String> analyzeRefTypeDesc(AbstractInsnNode insnNode) {
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
