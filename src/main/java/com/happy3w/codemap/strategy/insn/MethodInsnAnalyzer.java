package com.happy3w.codemap.strategy.insn;

import org.objectweb.asm.tree.MethodInsnNode;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class MethodInsnAnalyzer implements InsnAnalyzer<MethodInsnNode> {

    @Override
    public Class<MethodInsnNode> getInsnNodeType() {
        return MethodInsnNode.class;
    }

    @Override
    public Stream<String> refTypes(MethodInsnNode insnNode) {
        return Stream.of(insnNode.owner, insnNode.desc);
    }
}
