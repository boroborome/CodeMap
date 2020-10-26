package com.happy3w.codemap.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class JarLoader {

    public static Stream<InputStream> listAllClassesBytes(String jarFile) {
        return StreamSupport.stream(new ClassBytesSpliterator(jarFile), false);
    }

    private static class ClassBytesSpliterator extends Spliterators.AbstractSpliterator<InputStream> {
        private String jarFile;

        private ZipInputStream zin = null;
        private Enumeration<? extends ZipEntry> entryEnumeration;
        private ZipFile zipFile;

        protected ClassBytesSpliterator(String jarFile) {
            super(0, 0);
            this.jarFile = jarFile;
        }

        @Override
        public boolean tryAdvance(Consumer<? super InputStream> action) {
            try {
                if (entryEnumeration == null) {
                    entryEnumeration = initZin(jarFile);
                }
                ZipEntry entry = findNextClass(entryEnumeration);
                if (entry == null) {
                    return false;
                } else {
                    action.accept(zipFile.getInputStream(entry));
                    return true;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to load jar file:" + jarFile, e);
            }
        }

        private ZipEntry findNextClass(Enumeration<? extends ZipEntry> entryEnumeration) throws IOException {
            while (entryEnumeration.hasMoreElements()) {
                ZipEntry entry = entryEnumeration.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String fileName = entry.getName();
                if (!fileName.endsWith(".class")) {
                    continue;
                }
                return entry;
            }
            return null;
        }

        private Enumeration<? extends ZipEntry> initZin(String jarFile) throws IOException {
            zipFile = new ZipFile(jarFile);
            return zipFile.entries();
        }
    }
}
