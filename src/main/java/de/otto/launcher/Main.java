package de.otto.launcher;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {
        String className = null;
        if (args.length > 0) {
            className = args[0];
            if (className == null || className.trim().isEmpty()) {
                System.out.println("No main class found by launcher");
                System.out.println("Please specify a main class");
                System.exit(0);
            }
            try {
                Class<?> clazz = Class.forName(className);
                if (args.length > 1) {
                    String[] newArgs = new String[args.length -1];
                    System.arraycopy(args, 1, newArgs, 0, args.length);
                    clazz.getMethod("main", new Class<?>[] {String[].class}).invoke(null, new Object[]{newArgs});
                } else {
                    clazz.getMethod("main", new Class<?>[] {String[].class}).invoke(null, new Object[]{new String[]{}});
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please specify a main class");
            System.exit(0);
        }
    }

}
