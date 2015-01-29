import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StopWords {
	List<String> words;

	public StopWords() {
		words = new ArrayList<>();
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws IOException {
		File defaultFile = new File("./data/stopwords.in");
		if (defaultFile.canRead()) {
			BufferedReader br = new BufferedReader(new FileReader(defaultFile));
			String l = "";
			while ((l = br.readLine()) != null) {
				words.add(l.trim());
			}
			br.close();
		} else {
			// file does not exist or is not readable
			System.err.println("Default stopword file could not be opened!");
		}
	}

	public boolean isStopword(String word) {
		return words.contains(word);
	}
}