package src;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SuborderFreightCostCommandList {
	
	
	List<SuborderFreightCostCommand> suborderFreightCostCommandList;
	
	public List<SuborderFreightCostCommand> getSuborderFreightCostCommandList() {
		return suborderFreightCostCommandList;
	}
	
	public void setSuborderFreightCostCommandList(List<SuborderFreightCostCommand> suborderFreightCostCommandList) {
		this.suborderFreightCostCommandList = suborderFreightCostCommandList;
	}
	


}
