package de.patch.agent;

import de.patch.agent.compiler.JavaStringCompiler;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {

    public static void premain(String agentArgument, Instrumentation instrumentation) throws IOException {
        // load patched classes from files
        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results = new HashMap<>();

        File folder = new File("./patched_classes/");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Please put you patched classes in " + folder.getAbsolutePath() + " and restart.");
            System.exit(0);
        }
        System.out.println("Looking for patched java sources in " + folder.getAbsolutePath());
        File[] listOfFiles = folder.listFiles();
        Map<String, String> sourceMap = new HashMap<>();
        for (File srcFile : listOfFiles) {
            if (!srcFile.getName().contains(".java")) {
                continue;
            }
            String srcFileContent = new String(Files.readAllBytes(srcFile.toPath()));
            System.out.println("Adding patched class " + srcFile.getName());
            sourceMap.put(srcFile.getName(), srcFileContent);
        }
        try {
            Map<String, byte[]> compiledCode = compiler.compileFiles(sourceMap);
            results.putAll(compiledCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        instrumentation.addTransformer(new ClassPatchTransformer(results));
    }

}