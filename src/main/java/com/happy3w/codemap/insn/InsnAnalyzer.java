package com.happy3w.codemap.insn;

import java.util.stream.Stream;

public interface InsnAnalyzer <T>{

    Class<T> getInsnNodeType();

    Stream<String> refTypes(T insnNode);
}
