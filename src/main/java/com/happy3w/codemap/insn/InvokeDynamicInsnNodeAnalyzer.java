package com.happy3w.codemap.insn;

import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import java.util.stream.Stream;

public class InvokeDynamicInsnNodeAnalyzer implements InsnAnalyzer<InvokeDynamicInsnNode>{
    @Override
    public Class<InvokeDynamicInsnNode> getInsnNodeType() {
        return InvokeDynamicInsnNode.class;
    }

    @Override
    public Stream<String> refTypes(InvokeDynamicInsnNode insnNode) {
        return Stream.of(insnNode.desc);
    }
}
