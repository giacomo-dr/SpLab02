package ch.usi.delrig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.IntStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


public class JarAnalyzer {
	private static int[] invokeInstructions = {
			0xBA, 0xB9, 0xB7, 0xB8, 0xB6
	};
	
	private static int[] branchInstructions = {
			0xA5, 0xA6, 0x9F, 0xA2, 0xA3, 0xA4,
			0xA1, 0xA0, 0x99, 0x9C, 0x9D, 0x9E, 
			0x9B, 0x9A, 0xC7, 0xC6, 0x94, 0x96, 
			0x95, 0x98, 0x97,
			0xA8, 0xC9
	};

  public static void main(final String[] args) throws IOException {
	  
    if(args.length == 0) {
    	System.out.println("No input file.");
    	System.exit(1);
    }
    
    final String jarFileName = args[0]; 
    final JarFile jar = new JarFile( jarFileName );
    
//    System.out.println( "Analyzing " + jar.getName() );
    
    
    System.out.println( "Results\n" );
    System.out.println(analyzeJar(jar));
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
//      System.out.println( "  Class " + classNode.name );
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
	  
//	  System.out.println("    Method " + method.name + method.desc );
	  stats.numberOfMethodsWithCode++;
	  
	  AbstractInsnNode inst = method.instructions.getFirst();
	  while( inst != null ){
		  int opcode = inst.getOpcode();
		  if( opcode != -1 ){
			  stats.numberOfInstructions++;
			  stats.numberOfInstructionsByOpcode[opcode]++;
			  
			  if( isBranching(opcode) )
				  stats.numberOfBranchInstructions++;
			  
			  if( isInvocation(opcode) )
				  stats.numberOfMethodInvocations++;
		  }
		  inst = inst.getNext();
	  }
  }
  
  static boolean isBranching( int opcode ){
	  return IntStream.of(branchInstructions).anyMatch(code -> code == opcode);
  }
  
  static boolean isInvocation( int opcode ){
	  return IntStream.of(invokeInstructions).anyMatch(code -> code == opcode);
  }

}