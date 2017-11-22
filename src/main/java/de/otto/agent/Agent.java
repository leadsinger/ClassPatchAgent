package de.otto.agent;

import de.otto.agent.compiler.JavaStringCompiler;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.HashMap;
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
        for (File srcFile : listOfFiles) {
            String srcFileContent = new String(Files.readAllBytes(srcFile.toPath()));
            try {
                Map<String, byte[]> compiledCode = compiler.compile(srcFile.getName(), srcFileContent);
                results.putAll(compiledCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        instrumentation.addTransformer(new ClassPatchTransformer(results));
    }

}
