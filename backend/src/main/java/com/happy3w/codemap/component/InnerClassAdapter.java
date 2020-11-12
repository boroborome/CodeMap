package com.happy3w.codemap.component;

import com.happy3w.codemap.model.ClassRelation;
import com.happy3w.codemap.utils.ConstConfig;
import org.springframework.stereotype.Component;

@Component
public class InnerClassAdapter {
    public ClassRelation adapt(ClassRelation relation) {
        String sourceName = calculateMapName(relation.getClassA());
        String targetName = calculateMapName(relation.getClassB());

        if (sourceName != null || targetName != null) {
            return new ClassRelation(
                    sourceName == null ? relation.getClassA() : sourceName,
                    ClassRelation.REFERENCE,
                    targetName == null ? relation.getClassB() : targetName
            );
        }
        return relation;
    }


    private String calculateMapName(String name) {
        if (!ConstConfig.MergeAnonymityClass || name == null) {
            return null;
        }

        int startPos = name.lastIndexOf('/');
        if (startPos < 0) {
            startPos = 0;
        }
        int endPos = name.indexOf('$', startPos);
        if (endPos < 0) {
            return null;
        }
        return name.substring(0, endPos);
    }
}
