package com.happy3w.codemap.insn;

import org.objectweb.asm.tree.MethodInsnNode;

import java.util.stream.Stream;

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
