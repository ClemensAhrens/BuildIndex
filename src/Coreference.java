public class Coreference {
	String text;
	String chapId;
	Integer corefId;

	public Coreference(String text, String chapId, Integer corefId) {
		this.text = text;
		this.chapId = chapId;
		this.corefId = corefId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getChapId() {
		return chapId;
	}

	public void setChapId(String chapId) {
		this.chapId = chapId;
	}

	public Integer getCorefId() {
		return corefId;
	}

	public void setCorefId(Integer corefId) {
		this.corefId = corefId;
	}

}
