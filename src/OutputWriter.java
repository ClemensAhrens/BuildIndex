import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OutputWriter {

	File outFile;
	Map<String, ArrayList<Coreference>> multiMap;

	public OutputWriter(String filename) {
		outFile = new File(filename);
		multiMap = new HashMap<String, ArrayList<Coreference>>();
	}

	public void addCoreferences(Map<String, Coreference> coreferences) {
		Set<String> keySet = coreferences.keySet();
		for (String key : keySet) {
			// create ArrayList if key not already existent
			if (!multiMap.containsKey(key)) {
				multiMap.put(key, new ArrayList<Coreference>());
			}
			// add coreference for key to ArrayList in multiMap for key
			multiMap.get(key).add(coreferences.get(key));
		}
	}

	public void writeToFile() {
		// create output element, which will be turned to output XML later
		Document outputdoc = new Document(new Element("root"));

		// write basic elements
		Element chains = new Element("chains");
		Element outroot = outputdoc.getRootElement();
		outroot.addContent(chains);
		// ---- create sub elements for each coref in outputdoc
		for (String key : multiMap.keySet()) {
			Element chain = new Element("chain");
			chains.addContent(chain);
			chain.setAttribute("text", key);

			for (Coreference coreference : multiMap.get(key)) {
				Element coref = new Element("coreference");
				Element chapter = new Element("chapter");
				Element id = new Element("id");

				chain.addContent(coref);
				coref.addContent(id);
				id.addContent(coreference.getCorefId().toString());
				coref.addContent(chapter);
				chapter.addContent(coreference.getChapId());

			}
		}

		// format output XML file
		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());

		// ---- Write the complete result document to output XML file ----
		try {
			outp.output(outputdoc, new FileOutputStream(outFile));
		} catch (IOException e) {
			System.err.println("Error writing output file.");
			e.printStackTrace();
		}
	}
}
