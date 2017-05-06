package APIEmployeeJAXB.APIEmployeeJAXB;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class AllEmployee {
	@XmlElement
	private ArrayList<Employee> employees = null;

	public ArrayList<Employee> getemployees() {
		return employees;

	}

	public void setemployees(ArrayList<Employee> employees) {
		this.employees = employees;

	}
}
