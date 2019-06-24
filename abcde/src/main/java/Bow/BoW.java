package Bow;

import java.util.ArrayList;

import Storage.Storage;
import Storage.StorageObj;

public class BoW {
	private ArrayList<gramaticalClassList> classes;
	private ArrayList<String> moreRelevantTerms;
	private ArrayList<Term> countTerm;
	private ArrayList<gramaticalClassList> permanentClasses;

	public BoW() {
		this.moreRelevantTerms = new ArrayList<String>();
		this.classes = new ArrayList<gramaticalClassList>();
		this.countTerm = new ArrayList<Term>();
		this.permanentClasses = new ArrayList<gramaticalClassList>();
	}

	public void splitByClass(ArrayList<StorageObj> storage) {
		for (StorageObj storageObj : storage) {
			int exist = 0;
			for (gramaticalClassList gramaticalClass : classes) {
				if (gramaticalClass.getClassName().toUpperCase().contentEquals(storageObj.getClas().toUpperCase())) {
					exist = 1;
					GramaticalClass gc = new GramaticalClass(storageObj.getSplitQuestionNormalized(),
							storageObj.getSplitQuestionKind());
					gramaticalClass.addGramaticalClassList(gc);
				}
			}
			if (exist == 0) {
				gramaticalClassList gcl = new gramaticalClassList(storageObj.getClas());
				GramaticalClass gc = new GramaticalClass(storageObj.getSplitQuestionNormalized(),
						storageObj.getSplitQuestionKind());
				gcl.addGramaticalClassList(gc);
				classes.add(gcl);
			}

			exist = 0;
		}

	}

	public void addRelevantTerms(ArrayList<String> rvTerms) {
		moreRelevantTerms.addAll(rvTerms);
	}

	public void cleanIrrelevantTerms() {
		permanentClasses.addAll(classes);
		for (gramaticalClassList gramaticalClassList : classes) {
			for (GramaticalClass gramaticalClass : gramaticalClassList.getGramaticalClassList()) {
				int index = 0;
				while (index < gramaticalClass.getSplitQuestionKind().size()) {
					if (moreRelevantTerms.contains(gramaticalClass.getSplitQuestionKind().get(index)) == false) {
						gramaticalClass.removeByIndex(index);
					} else {
						index++;
					}
				}
			}

		}
		
	}

	public void countTerms() {
		for (gramaticalClassList gramaticalClassList : classes) {
			for (GramaticalClass gramaticalClass : gramaticalClassList.getGramaticalClassList()) {
				for (String string : gramaticalClass.getSplitQuestionNormalized()) {
					int aux = 0;
					for (Term term : countTerm) {
						if (term.getTerm().contentEquals(string)) {
							term.addCount();
							aux = 1;
						}

					}
					if (aux == 0) {
						Term term = new Term(string);
						term.addCount();
						countTerm.add(term);
					}
				}
			}
		}
		
	}

	public void setTerms(int k) {
		moreRelevantTerms.clear();
		int aux = 0;
		String auxstring = "";
		for (int i = 0; i < k; i++) {
			for (Term ct : countTerm) {
				if(moreRelevantTerms.contains(ct.getTerm())==false && ct.getCount()>aux) {
					aux = ct.getCount();
					auxstring = ct.getTerm();
				}
			}
			moreRelevantTerms.add(auxstring);
			aux=0;
		}
	}

	public ArrayList<String> getMoreRelevantTerms() {
		return moreRelevantTerms;
	}
	
	public ArrayList<gramaticalClassList> getPermanentClasses() {
		return permanentClasses;
	}

	public ArrayList<String> getQuestionNormalized(Storage storage, String question, String clas) {
		return storage.oneNormalization(question, clas);
	}
}
