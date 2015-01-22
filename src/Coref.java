import java.util.ArrayList;
import java.util.Map;

public class Coref {
	ArrayList<String> inFiles;
	String outFile;

	// parser for inputs
	void parseArgs(String[] args) {
		Integer iIdx = null;
		Integer oIdx = null;
		inFiles = new ArrayList<String>();

		// input must come before output ---
		// maybe TODO: search pretty
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-i")) {
				iIdx = new Integer(i);
			} else if (args[i].equals("-o")) {
				oIdx = new Integer(i);
			}
		}
		if (iIdx != null & oIdx != null) {
			outFile = args[oIdx + 1];
			for (int i = iIdx + 1; i < oIdx; i++) {
				inFiles.add(args[i]);
			}
		} else {
			System.out.println("Usage: " + "\n"
					+ "java Coref -i <XmlFile> -o <NewFile>" + "\n"
					+ "Example (Linux): " + "\n"
					+ "java Coref -i ../Input/*.xml -o ../output.xml");
			System.exit(-1); // TODO: search exit status for failure
		}
	}

	public static void main(String[] args) {
		Coref coref = new Coref();
		coref.parseArgs(args);

		OutputWriter outputWriter = new OutputWriter(coref.getOutFile());

		// for input file
		try {
			for (String inFile : coref.getInFiles()) {
				InputAnalyzer inputAnalyzer = new InputAnalyzer(inFile);

				Map<String, Coreference> coreferences = inputAnalyzer
						.extractCoreferences();

				outputWriter.addCoreferences(coreferences);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		outputWriter.writeToFile();

	}

	public ArrayList<String> getInFiles() {
		return inFiles;
	}

	public String getOutFile() {
		return outFile;
	}
}