package com.happy3w.codemap.component;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class SignatureAnalyzer {

    public Stream<String> analyzeTypes(String... combineTypeDesc) {
        return analyzeTypes(Stream.of(combineTypeDesc));
    }

    public Stream<String> analyzeTypes(Stream<String> combineTypeDesc) {
        return combineTypeDesc
                .filter(Objects::nonNull)
                .distinct()
                .flatMap(combineType -> analyzeType(combineType).stream())
                .distinct()
                .filter(typeName -> !isJavaPlantformType(typeName));
    }

    private List<String> analyzeType(String combineTypeDesc) {
        if (combineTypeDesc == null || combineTypeDesc.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> types = new ArrayList<>();
        char[] typeChs = combineTypeDesc.toCharArray();
        for (int chIndex = 0; chIndex < typeChs.length; chIndex++) {
            chIndex = ignoreStructCh(typeChs, chIndex);
            int typeNameEndIndex = findNameEnd(typeChs, chIndex);
            if (typeNameEndIndex < 0) {
                break;
            }
            String typeName = combineTypeDesc.substring(chIndex, typeNameEndIndex);
            if (typeName.length() > 1
                    && typeName.charAt(0) == 'T'
                    && Character.isUpperCase(typeName.charAt(1))) {
                typeName = typeName.substring(1);
            }
            types.add(typeName);
            chIndex = typeNameEndIndex;
        }
        return types;
    }

    private int findNameEnd(char[] typeChs, int chIndex) {
        int index = chIndex;
        for (; index < typeChs.length; index++) {
            char ch = typeChs[index];
            if (!isIdentity(ch)) {
                return index;
            }
        }
        return index == chIndex ? -1 : index;
    }

    private boolean isIdentity(char ch) {
        return ch == '/' || Character.isJavaIdentifierPart(ch);
    }

    private int ignoreStructCh(char[] typeChs, int chIndex) {
        for (; chIndex < typeChs.length; chIndex++) {
            char ch = typeChs[chIndex];
            if (Character.isUpperCase(ch) || !isIdentity(ch)) {
                continue;
            }
            break;
        }
        return chIndex;
    }

    // TODO 以后单独提出来作为Filter处理
    public boolean isJavaPlantformType(String dataType) {
        return dataType.length() == 1
                || dataType.startsWith("java/");
    }
}
