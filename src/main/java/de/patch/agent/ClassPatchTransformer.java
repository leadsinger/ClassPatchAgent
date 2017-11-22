package de.patch.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;

public class ClassPatchTransformer implements ClassFileTransformer {

    private Map<String, byte[]> patchedClasses;

    public ClassPatchTransformer(Map<String, byte[]> patchedClasses) {
        this.patchedClasses = patchedClasses;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
       String classNameWithDots = className.replaceAll("/", ".");
        if (patchedClasses.containsKey(classNameWithDots)) {
            System.out.println("Patching class: " + classNameWithDots);
            return patchedClasses.get(classNameWithDots);
        }
        return null;
    }
}
