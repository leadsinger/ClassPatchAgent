**Usage:**

1. Import the project into your favourite IDE with a decompiler plugin
2. Put all your dependencies in the libs folder
3. Put all patched classes (as *.java file) in the patched_classes folder
4. Put the main class into pom.xml and run mvn install exec:exec
5. A debug server is started on port 1044 (it is not suspended, 
change it in pom.xml if needed)

NOTE: you can also run it as a standalone project, just have a look at pom.xml
