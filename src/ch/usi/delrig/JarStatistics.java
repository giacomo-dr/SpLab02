package ch.usi.delrig;

public class JarStatistics {
	public String jarFilename = "";
	public int numberOfClasses = 0;
	public int numberOfMethodsWithCode = 0;
	public int numberOfInstructions = 0;
	public int[] numberOfInstructionsByOpcode = new int[256];
	public int numberOfMethodInvocations = 0;
	public int numberOfBranchInvocations = 0;
}
