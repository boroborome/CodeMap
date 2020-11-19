package com.happy3w.codemap.component;

import com.happy3w.codemap.model.BackendTask;
import com.happy3w.codemap.model.ClassRelation;
import com.happy3w.codemap.model.CmWorkspace;
import com.happy3w.codemap.utils.JarLoader;
import com.happy3w.persistence.es.EsAssistant;
import com.happy3w.toolkits.utils.FileUtils;
import org.objectweb.asm.ClassReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

@Component
public class WorkspaceAnalyzer {
    private final RelationAnalyzer relationAnalyzer;
    private final EsAssistant esAssistant;

    public WorkspaceAnalyzer(RelationAnalyzer relationAnalyzer, EsAssistant esAssistant) {
        this.relationAnalyzer = relationAnalyzer;
        this.esAssistant = esAssistant;
    }

    public void analyze(CmWorkspace newWorkspace, BackendTask task) {
        newWorkspace.getFileToAnalyze().stream()
                .map(File::new)
                .filter(File::exists)
                .flatMap(FileUtils::enumerateFiles)
                .filter(file -> file.getName().endsWith(".jar"))
                .forEach(this::analyzeJarFile);
    }

    private void analyzeJarFile(File jarFile) {
        String shortName = jarFile.getName();
        Stream<ClassRelation> relations = JarLoader.listAllClassesBytes(jarFile.getAbsolutePath())
                .map(this::createClassReader)
                .flatMap(relationAnalyzer::collectRelations)
                .peek(relation -> {
                    relation.setJarFile(shortName);
                    relation.initId();
                });
        esAssistant.saveStream(relations);
    }

    private ClassReader createClassReader(InputStream inputStream) {
        try {
            return new ClassReader(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load class data.");
        }
    }
}
