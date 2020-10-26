package com.happy3w.codemap;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.stream.Collectors;

public class RelationAnalyzerTest {

    @Test
    public void should_collect_annotation_on_class_success() throws IOException {
        ClassReader classReader = new ClassReader(AnnotationClass.class.getName());
        String result = new RelationAnalyzer().collectRelations(classReader)
                .collect(Collectors.toList())
                .toString();
        Assert.assertEquals("[com/happy3w/codemap/AnnotationClass-null->null, com/happy3w/codemap/AnnotationClass-reference->com/happy3w/codemap/DemoAnnotation]",
                result);
    }

}
