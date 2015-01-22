import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class InputAnalyzer {

	File infile;

	public InputAnalyzer(String filename) throws Exception {
		infile = new File(filename);
		if (!infile.exists()) {
			throw new Exception("Fle " + filename + " not found");
		}
	}

	public Map<String, Coreference> extractCoreferences() {
		Map<String, Coreference> map = new HashMap<String, Coreference>();
		// aufrufen des builders pro input
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(infile);

			// ---- Create list of <coreferences> & extract chapID
			Element root = doc.getRootElement();
			String chapID = root.getAttribute("id").getValue(); // extract chapter argument ID 
//			Element document = root.getChild("document"); // jump to necessary level of sub elements
//			String chapID = document.getAttribute("chap").getValue(); // extract
																		// chapID
																		// for
																		// outputdoc
			Element corefs = root.getChild("coreferences");
			@SuppressWarnings("unchecked")
			List<Element> listcoref = corefs.getChildren("coreference");
			for (Element coref : listcoref) {
				String corefID = coref.getAttribute("id").getValue();
				@SuppressWarnings("unchecked")
				List<Element> listmention = coref.getChildren("mention");
				for (Element mention : listmention) {
					if (null == mention)
						continue;
					String s = mention.getAttributeValue("representative"); // <AttrS
					if (null == s || !s.equals("true"))
						continue; // <ValS>
					String text = mention.getChildText("text");
					Coreference coreference = new Coreference(text, chapID, Integer.parseInt(corefID));
					map.put(text, coreference);

				}
			}
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e){
			System.err.println("Error while parsing corefID: \n" + e.getMessage());
		}

		return map;

	}

	public ArrayList<Element> extractCoreferences2() {
		ArrayList<Element> arrayList = new ArrayList<Element>();

		// aufrufen des builders pro input
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(infile);

			// ---- Create list of <coreferences> & extract chapID
			Element root = doc.getRootElement();
			Element document = root.getChild("document");
			String chapID = document.getAttribute("chap").getValue(); // extract
																		// chapID
																		// for
																		// outputdoc
			Element corefs = document.getChild("coreferences");
			List listcoref = corefs.getChildren("coreference");

			// ---- create list of <mention> for each listcoref & extract
			// corefID
			for (int i = 0; i < listcoref.size(); i++) {
				Element coref = (Element) (listcoref.get(i));
				String corefID = coref.getAttribute("id").getValue(); // extract
																		// corefID
																		// for
																		// outputdoc
				List listmention = coref.getChildren("mention");

				// Find <mention> with given attribute representative="true":
				for (int j = 0; j < listmention.size(); j++) {
					Element ment = (Element) (listmention.get(j));
					if (null == ment)
						continue;
					String s = ment.getAttributeValue("representative"); // <AttrS
					if (null == s || !s.equals("true"))
						continue; // <ValS>
					String text = ment.getChildText("text");

					// test check for correct xml-element:
					// ment.setAttribute("testattr", "testval"); //
					// <AttrNew>=<ValNew>

					// ---- create sub elements for each coref in outputdoc
					Element newChain = new Element("chain");
					Element newCoref = new Element("coreference");
					Element newChapter = new Element("chapter");
					Element newID = new Element("id");

					// write results in output

					newChain.setAttribute("reprText", text); // insert <text>
																// from
																// representative
																// <mention> as
																// attribute
					newChain.addContent(newCoref);
					newCoref.addContent(newChapter);
					newChapter.addContent(chapID);
					newCoref.addContent(newID);
					newID.addContent(corefID);

					arrayList.add(newChain);
				}
			}

		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrayList;
	}

}
