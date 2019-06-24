package Bow;

import java.util.ArrayList;

public class GramaticalClass {
	private ArrayList<String> splitQuestionNormalized;
	private ArrayList<String> splitQuestionKind;

	public GramaticalClass(ArrayList<String> splitQuestionNormalized, ArrayList<String> splitQuestionKind) {
		this.setSplitQuestionNormalized(splitQuestionNormalized);
		this.setSplitQuestionKind(splitQuestionKind);
	}

	public ArrayList<String> getSplitQuestionNormalized() {
		return splitQuestionNormalized;
	}

	public void setSplitQuestionNormalized(ArrayList<String> splitQuestionNormalized) {
		this.splitQuestionNormalized = splitQuestionNormalized;
	}

	public ArrayList<String> getSplitQuestionKind() {
		return splitQuestionKind;
	}

	public void setSplitQuestionKind(ArrayList<String> splitQuestionKind) {
		this.splitQuestionKind = splitQuestionKind;
	}

	public void removeByIndex(int index) {
		splitQuestionKind.remove(index);
		splitQuestionNormalized.remove(index);
	}
}
