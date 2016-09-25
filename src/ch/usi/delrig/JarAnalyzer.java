package ch.usi.delrig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


public class JarAnalyzer {

  public static void main(final String[] args) throws IOException {
	final String jarFileName = args[0]; 
    final JarFile jar = new JarFile( jarFileName );
    System.out.println( "Analyzing " + jar.getName() );
    analyzeJar( jar );
    
    System.out.println( "Results" );
    //TODO Show statistics
  }
  
  private static JarStatistics analyzeJar( JarFile jar ) throws IOException{
	  JarStatistics stats = new JarStatistics();
	  stats.jarFilename = jar.getName();
	  
	  final Enumeration<JarEntry> entries = jar.entries();
	  while( entries.hasMoreElements() ){
		  final JarEntry entry = entries.nextElement();
		  if( !entry.isDirectory() && entry.getName().endsWith(".class") ){
			  final InputStream is = jar.getInputStream( entry );
			  final ClassReader classReader = new ClassReader( is );
			  analyzeClass( classReader, stats );
	      }
	  }
	  
	  return stats;
  }
  
  private static void analyzeClass( ClassReader classReader, JarStatistics stats ){
	  final ClassNode classNode = new ClassNode();
      classReader.accept( classNode, ClassReader.SKIP_FRAMES );
      System.out.println( "  Class " + classNode.name );
	  stats.numberOfClasses++;
      
      @SuppressWarnings("unchecked")
	  final List<MethodNode> methods = classNode.methods;
      for( final MethodNode methodNode : methods ){
    	  analyzeMethod( methodNode, stats );
      }
  }
  
  private static void analyzeMethod( MethodNode method, JarStatistics stats ){
	  if( method.instructions.size() == 0 )
		  return; // Skip empty methods
	  
	  System.out.println("    Method " + method.name + method.desc );
	  stats.numberOfMethodsWithCode++;
	  
	  AbstractInsnNode inst = method.instructions.getFirst();
	  while( inst != null ){
		  int opcode = inst.getOpcode();
		  if( opcode != -1 ){
			  stats.numberOfInstructions++;
			  stats.numberOfInstructionsByOpcode[opcode]++;
			  
			  if( isBranching(opcode) )
				  stats.numberOfBranchInvocations++;
			  
			  if( isInvocation(opcode) )
				  stats.numberOfMethodInvocations++;
		  }
		  inst = inst.getNext();
	  }
  }
  
  static boolean isBranching( int opcode ){
	  // TODO Check whether opcode is a branch or not excl. goto
	  return false;
  }
  
  static boolean isInvocation( int opcode ){
	  // TODO Check whether opcode is method call
	  return false;
  }

}