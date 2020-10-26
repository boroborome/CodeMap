package com.happy3w.codemap.strategy.insn;

import org.objectweb.asm.tree.FieldInsnNode;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class FieldInsnNodeAnalyzer implements InsnAnalyzer<FieldInsnNode>{
    @Override
    public Class<FieldInsnNode> getInsnNodeType() {
        return FieldInsnNode.class;
    }

    @Override
    public Stream<String> refTypes(FieldInsnNode insnNode) {
        return Stream.of(insnNode.owner, insnNode.desc);
    }
}
