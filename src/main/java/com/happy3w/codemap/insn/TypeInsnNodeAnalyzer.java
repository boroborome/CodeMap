package com.happy3w.codemap.insn;

import org.objectweb.asm.tree.TypeInsnNode;

import java.util.stream.Stream;

public class TypeInsnNodeAnalyzer implements InsnAnalyzer<TypeInsnNode>{
    @Override
    public Class<TypeInsnNode> getInsnNodeType() {
        return TypeInsnNode.class;
    }

    @Override
    public Stream<String> refTypes(TypeInsnNode insnNode) {
        return Stream.of(insnNode.desc);
    }
}
