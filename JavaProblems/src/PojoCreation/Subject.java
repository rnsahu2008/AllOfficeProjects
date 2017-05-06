package PojoCreation;


public class Subject {
	
	private String subjectname;
	private String subjectId;
	
	public String getSubjectname() {
		return subjectname;
	}

	public Subject(String subjectname, String subjectId) {
		this.subjectname = subjectname;
		this.subjectId = subjectId;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Subject() {
	}

}