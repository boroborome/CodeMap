package com.happy3w.codemap;

import com.happy3w.codemap.component.RelationAnalyzer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.objectweb.asm.ClassReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RelationAnalyzerTest {

    @Autowired
    private RelationAnalyzer relationAnalyzer;

    @Test
    public void should_collect_annotation_on_class_success() throws IOException {
        ClassReader classReader = new ClassReader(AnnotationClass.class.getName());
        String result = relationAnalyzer.collectRelations(classReader)
                .collect(Collectors.toList())
                .toString();
        Assert.assertEquals("[com/happy3w/codemap/AnnotationClass-null->null, com/happy3w/codemap/AnnotationClass-reference->com/happy3w/codemap/DemoAnnotation]",
                result);
    }

}
