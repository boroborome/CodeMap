package com.happy3w.codemap.insn;

import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import java.util.Arrays;
import java.util.stream.Stream;

public class InvokeDynamicInsnNodeAnalyzer implements InsnAnalyzer<InvokeDynamicInsnNode> {
    @Override
    public Class<InvokeDynamicInsnNode> getInsnNodeType() {
        return InvokeDynamicInsnNode.class;
    }

    @Override
    public Stream<String> refTypes(InvokeDynamicInsnNode insnNode) {
        return Stream.concat(
                Stream.of(insnNode.desc, insnNode.bsm.getOwner()),
                Arrays.asList(insnNode.bsmArgs).stream()
                        .filter(handle -> handle instanceof Handle)
                        .map(handle -> ((Handle) handle).getOwner())
        );
    }
}
