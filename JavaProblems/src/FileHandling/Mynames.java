package FileHandling;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Mynames {
	@XmlElement
	private ArrayList<name> names = null;

	public ArrayList<name> getnames() {
		return names;

	}

	public void setemployees(ArrayList<name> names) {
		this.names = names;

	}
}
