package com.happy3w.codemap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypeAnalyzer {
    public static List<String> analyzeTypes(String combineTypeDesc) {
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
            types.add(combineTypeDesc.substring(chIndex, typeNameEndIndex));
            chIndex = typeNameEndIndex;
        }
        return types;
    }

    private static int findNameEnd(char[] typeChs, int chIndex) {
        int index = chIndex;
        for (; index < typeChs.length; index++) {
            char ch = typeChs[index];
            if (ch == '['
                    || ch == '<'
                    || ch == '>'
                    || ch == ';'
            ) {
                return index;
            }
        }
        return index == chIndex ? -1 : index;
    }

    private static int ignoreStructCh(char[] typeChs, int chIndex) {
        for (; chIndex < typeChs.length; chIndex++) {
            char ch = typeChs[chIndex];
            if (ch == 'L'
                    || ch == '['
                    || ch == '<'
                    || ch == '>'
                    || ch == ';'
            ) {
                continue;
            }
            break;
        }
        return chIndex;
    }
}
