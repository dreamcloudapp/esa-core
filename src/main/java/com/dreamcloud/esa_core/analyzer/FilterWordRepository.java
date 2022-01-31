package com.dreamcloud.esa_core.analyzer;

import org.apache.lucene.analysis.CharArraySet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FilterWordRepository {
    boolean ignoreCase;
    boolean readSources = false;
    protected CharArraySet source;
    protected ArrayList<File> sourceFiles = new ArrayList<>();
    protected ArrayList<String> sourceFileNames = new ArrayList<>();
    private ArrayList<InputStream> inputStreams = new ArrayList<>();

    public FilterWordRepository(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        this.source = new CharArraySet(256, ignoreCase);
    }

    public void addSource(CharArraySet source) {
        this.source.addAll(source);
        this.readSources = false;
    }

    public void addSource(InputStream inputStream) {
        this.inputStreams.add(inputStream);
    }

    public void addSource(File sourceFile) {
        this.sourceFiles.add(sourceFile);
        this.readSources = false;
    }

    public void addSource(String sourceFileName) {
        this.sourceFileNames.add(sourceFileName);
        this.readSources = false;
    }

    public void loadSourceFileNames(String sourceFileName) throws IOException {
        this.loadSourceFile(new File(sourceFileName));
    }

    public void loadSourceFile(File sourceFile) throws IOException {
        InputStream fileInputStream = new FileInputStream(sourceFile);
        this.loadFromInputStream(fileInputStream);
    }

    private void loadFromInputStream(InputStream inputStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        String line = reader.readLine();
        while (line != null) {
            if (!"".equals(line)) {
                source.add(line);
            }
            line = reader.readLine();
        }
        reader.close();
    }

    public CharArraySet getWords() throws IOException {
        if (!readSources) {
            for (InputStream inputStream: inputStreams) {
                this.loadFromInputStream(inputStream);
            }
            for (File sourceFile: sourceFiles) {
                this.loadSourceFile(sourceFile);
            }
            for (String sourceFileName: sourceFileNames) {
                this.loadSourceFileNames(sourceFileName);
            }
            readSources = true;
        }
        return source;
    }
}
