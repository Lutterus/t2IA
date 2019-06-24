package Bow;

import java.util.ArrayList;

public class gramaticalClassList {
	private ArrayList<GramaticalClass> gramaticalClassList;
	private String className;
	public gramaticalClassList(String className) {
		this.setGramaticalClassList(new ArrayList<GramaticalClass>());
		this.setClassName(className);
	}
	public ArrayList<GramaticalClass> getGramaticalClassList() {
		return gramaticalClassList;
	}
	public void setGramaticalClassList(ArrayList<GramaticalClass> gramaticalClassList) {
		this.gramaticalClassList = gramaticalClassList;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void addGramaticalClassList(GramaticalClass gc) {
		gramaticalClassList.add(gc);
	}
}
