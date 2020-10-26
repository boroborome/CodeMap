package com.happy3w.codemap.strategy.insn;

import org.objectweb.asm.tree.TypeInsnNode;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
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
