package ch.usi.delrig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


public class JarAnalyzer {

  public static void main(final String[] args) throws IOException {
    final String jarFileName = args[0]; 
    System.out.println("Analyzing "+jarFileName);
    final JarFile jar = new JarFile(jarFileName);
    final Enumeration<JarEntry> entries = jar.entries();
    while (entries.hasMoreElements()) {
      final JarEntry entry = entries.nextElement();
      if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
        System.out.println(entry.getName());
        final InputStream is = jar.getInputStream(entry);
        final ClassReader classReader = new ClassReader(is);
        final ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.SKIP_FRAMES);
        System.out.println("  Class "+classNode.name);                          
        final List<MethodNode> methods = classNode.methods;
        for (final MethodNode methodNode : methods) {
          System.out.println("    Method "+methodNode.name+methodNode.desc);
          //...
        }
      }
    }
  }
  
  private static void analyzeJar( JarFile jar ){
	  
  }

}