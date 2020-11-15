package com.happy3w.codemap;

import com.happy3w.codemap.component.RelationAnalyzer;
import com.happy3w.codemap.utils.JarLoader;
import com.happy3w.codemap.utils.ResultWriter;
import com.happy3w.persistence.es.EsAssistant;
import com.happy3w.persistence.es.model.EsConnectConfig;
import org.objectweb.asm.ClassReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

@SpringBootApplication
public class CodeMapApplication {


    public static void main(String[] args) {
        SpringApplication.run(CodeMapApplication.class, args);
    }

    @ConfigurationProperties(prefix = "code-map.es")
    @Bean
    public EsConnectConfig esConnectionConfig() {
        return new EsConnectConfig();
    }

    @Bean
    public EsAssistant esAssistant(EsConnectConfig esConnectionConfig) throws UnknownHostException {
        return EsAssistant.from(esConnectionConfig);
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
