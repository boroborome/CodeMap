package com.happy3w.codemap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class ResultWriter {
    private String resultDirPath;

    private Set<String> existClass = new HashSet<>();
    private PrintStream nodePrint;
    private PrintStream edgesPrint;

    public ResultWriter(String resultDirPath) {
        this.resultDirPath = resultDirPath;
    }

    public void start() throws FileNotFoundException {
        File resultDir = new File(resultDirPath);
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }

        File indexFile = new File(resultDir, "index.html");
        copyFile("/template.index.html", indexFile);

        File categoriesFile = new File(resultDir, "categories.js");
        copyFile("/template.categories.js", categoriesFile);

        nodePrint = new PrintStream(new FileOutputStream(new File(resultDir, "nodes.js")));
        edgesPrint = new PrintStream(new FileOutputStream(new File(resultDir, "edges.js")));

        nodePrint.println("var nodes = [");
        edgesPrint.println("var edges = [");
    }

    private void copyFile(String resourceName, File outputFile) {
        InputStream resourceStream = ResultWriter.class.getResourceAsStream(resourceName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
            byte[] buff = new byte[1024];
            for (int size = resourceStream.read(buff);
                 size > 0;
                 size = resourceStream.read(buff)) {
                outputStream.write(buff, 0, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void write(ClassRelation classRelation) {
        writeNode(classRelation.getClassA());
        writeNode(classRelation.getClassB());
        writeEdges(classRelation);
    }

    private void writeEdges(ClassRelation classRelation) {
        if (classRelation.getClassB() == null || classRelation.getClassA() == null) {
            return;
        }
        edgesPrint.println(String.format("{source: '%s',type:'%s',target:'%s'},",
                classRelation.getClassA(),
                classRelation.getRelation(),
                classRelation.getClassB()));
    }

    private void writeNode(String classType) {
        if (classType == null || classType.isEmpty() || existClass.contains(classType)) {
            return;
        }
        existClass.add(classType);

        String shortName = createShortName(classType);
        nodePrint.println(String.format("{id: '%s',name:'%s'},",
                classType, shortName));
    }

    private String createShortName(String classType) {
        int lastPointIndex = classType.lastIndexOf('/');
        if (lastPointIndex < 0) {
            return classType;
        }
        return classType.substring(lastPointIndex + 1);
    }

    public void close() {
        if (nodePrint != null) {
            nodePrint.println("];");
            nodePrint.close();
        }
        if (edgesPrint != null) {
            edgesPrint.println("];");
            edgesPrint.close();
        }
    }
}
