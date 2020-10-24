package com.happy3w.codemap;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;

public class CodeMapApplication {
    public static void main(String[] args) throws IOException {
//        SpringApplication.run(CodeMapApplication.class, args);
        if (args.length < 2) {
            System.out.println("Usage: java -jar CodeMap.jar xxx.jar index.html");
            System.exit(1);
        }

        String jarFile = args[0];
        String outHtml = args[1];

        ResultWriter writer = new ResultWriter(outHtml);
        RelationAnalyzer analyzer = new RelationAnalyzer();

        writer.start();
        JarLoader.listAllClassesBytes(jarFile)
                .map(CodeMapApplication::createClassReader)
                .flatMap(analyzer::collectRelations)
                .forEach(writer::write);
        writer.close();
    }

    private static ClassReader createClassReader(InputStream inputStream) {
        try {
            return new ClassReader(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load class data.");
        }
    }
}
