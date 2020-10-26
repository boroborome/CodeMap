package com.happy3w.codemap;

import org.objectweb.asm.ClassReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class CodeMapApplication implements CommandLineRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CodeMapApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
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
