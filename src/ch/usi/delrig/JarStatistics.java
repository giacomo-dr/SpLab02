package ch.usi.delrig;

import org.objectweb.asm.util.Printer;

public class JarStatistics {
	public String jarFilename = "";
	public int numberOfClasses = 0;
	public int numberOfMethodsWithCode = 0;
	public int numberOfInstructions = 0;
	public int[] numberOfInstructionsByOpcode = new int[200];
	public int numberOfMethodInvocations = 0;
	public int numberOfBranchInstructions = 0;
	
	@Override
	public String toString() {
		return  "File name: \t\t\t\t" + jarFilename + '\n' +
				"Number of classes: \t\t\t" + Integer.toString(numberOfClasses) + "\n" +
				"Number of methods with code: \t\t" + Integer.toString(numberOfMethodsWithCode) + "\n" +
				"Number of method invocations: \t\t" + Integer.toString(numberOfMethodInvocations) + "\n" +
				"Number of instructions: \t\t" + Integer.toString(numberOfInstructions) + "\n" +
				"Number of branch invocations: \t\t" + Integer.toString(numberOfBranchInstructions) + "\n" +
				"Number of instructions by opcode:\n" + printNumberOfInstructionsByOpcode();
				
	}
	
	private String printNumberOfInstructionsByOpcode() {
		String result = ""; 
		for(int i = 0; i < numberOfInstructionsByOpcode.length; i++) {
			String opCodeName = Printer.OPCODES[i];
			if(!opCodeName.equals("")) {
				result += "\t" + opCodeName + ": " + Integer.toString(numberOfInstructionsByOpcode[i]) + "\n";
			}
			
		}
		return result;
	}
}
