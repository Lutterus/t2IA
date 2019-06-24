package Bow;

public class Term {
	private String term;
	private int count;

	public Term(String term) {
		this.setTerm(term);
		this.count=0;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getCount() {
		return count;
	}

	public void addCount() {
		count = count + 1;
	}
}
