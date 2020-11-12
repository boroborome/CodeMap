package com.happy3w.codemap;

import com.happy3w.codemap.component.RelationAnalyzer;
import com.happy3w.codemap.utils.JarLoader;
import com.happy3w.codemap.utils.ResultWriter;
import org.objectweb.asm.ClassReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class CodeMapApplication {


    public static void main(String[] args) {
        SpringApplication.run(CodeMapApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(name = "com.happy3w.code-map.cmdline-mode", havingValue = "true", matchIfMissing = true)
    public CommandStarter commandStarter() {
        return new CommandStarter();
    }

    public static class CommandStarter implements CommandLineRunner {
        @Autowired
        private RelationAnalyzer relationAnalyzer;

        @Override
        public void run(String... args) throws Exception {
            if (args.length < 2) {
                System.out.println("Usage: java -jar CodeMap.jar xxx.jar index.html");
                System.exit(1);
            }

            String jarFile = args[0];
            String outHtml = args[1];

            ResultWriter writer = new ResultWriter(outHtml);

            writer.start();
            JarLoader.listAllClassesBytes(jarFile)
                    .map(this::createClassReader)
                    .flatMap(relationAnalyzer::collectRelations)
                    .forEach(writer::write);
            writer.close();
        }

        private ClassReader createClassReader(InputStream inputStream) {
            try {
                return new ClassReader(inputStream);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to load class data.");
            }
        }
    }
}
