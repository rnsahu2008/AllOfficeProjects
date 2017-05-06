package PojoCreation;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/** * This class represents a Student. * @author javawithease */
@XmlRootElement
public class Student {
	private String name;
	private String rollNo;
	private String className;
	private int age;
	private int id;
	private List<Subject> subjects;

	// Default constructor
	public Student() {
	}

	// Parameterised constructor
	public Student(String name, String rollNo, String className, int age,
			int id, List<Subject> subject) {

		this.name = name; 
		this.rollNo = rollNo;
		this.className = className;
		this.age = age;
		this.id = id;
		this.subjects = subject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

}
