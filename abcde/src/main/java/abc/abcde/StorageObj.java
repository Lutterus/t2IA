package abc.abcde;

import java.util.ArrayList;

public class StorageObj {
	private String question;
	private String answer;
	private String clas;
	private ArrayList<String> splitQuestion;
	private ArrayList<String> splitQuestionNormalized;
	private ArrayList<String> splitQuestionKind;
	
	public StorageObj() {
		splitQuestion = new ArrayList<String>();
		splitQuestionNormalized = new ArrayList<String>();
		splitQuestionKind = new ArrayList<String>();
	}

	public String getQuesion() {
		return question;
	}

	public void setQuesion(String quesion) {
		this.question = quesion;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getClas() {
		return clas;
	}

	public void setClas(String clas) {
		this.clas = clas;
	}

	public ArrayList<String> getSplitQuestion() {
		return splitQuestion;
	}
	
	public void addSplitQuestion(String string) {
		splitQuestion.add(string);
	}

	public ArrayList<String> getSplitQuestionNormalized() {
		return splitQuestionNormalized;
	}
	
	public void addQuestionNormalized(String string) {
		splitQuestionNormalized.add(string);
	}

	public ArrayList<String> getSplitQuestionKind() {
		return splitQuestionKind;
	}
	
	public void addSplitQuestionKind(String string) {
		splitQuestionKind.add(string);
	}
	
	public void myToString() {
		System.out.println("Question: " + question);
		System.out.println("Answer: " + answer);
		System.out.println("Class: " + clas);
		
		String temp = "";	
		for (String string : splitQuestion) {
			temp = temp + "-" + string;
		}
		System.out.println("splitQuestion: " + temp);

		temp = "";
		for (String string : splitQuestionNormalized) {
			temp = temp + "-" + string;
		}
		System.out.println("splitQuestionNormalized: " + temp);

		temp = "";
		for (String string : splitQuestionKind) {
			temp = temp + "-" + string;
		}
		System.out.println("splitQuestionKind: " + temp);
		
	}

}
