package de.patch.agent.compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.*;

public class JavaStringCompiler {

    JavaCompiler compiler;
    StandardJavaFileManager stdManager;

    public JavaStringCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    public Map<String, byte[]> compileFiles(Map<String, String> sourceMap) throws IOException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            List<JavaFileObject> files = new ArrayList<>();
            Set<String> fileNames = sourceMap.keySet();
            for (String fileName : fileNames) {
                JavaFileObject javaFileObject = manager.makeStringSource(fileName, sourceMap.get(fileName));
                files.add(javaFileObject);
            }
            CompilationTask task = compiler.getTask(null, manager, null, null, null, files);
            Boolean result = task.call();
            if (result == null || !result.booleanValue()) {
                throw new RuntimeException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }

    /**
     * Load class from compiled classes.
     *
     * @param name
     *            Full class name.
     * @param classBytes
     *            Compiled results as a Map.
     * @return The Class instance.
     * @throws ClassNotFoundException
     *             If class not found.
     * @throws IOException
     *             If load error.
     */
    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            return classLoader.loadClass(name);
        }
    }
}