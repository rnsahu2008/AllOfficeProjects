package src;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class SuborderFreightCostCommand {
	private String username;
	private Long fromPincode; 
	private Long toPincode; 
	private BigDecimal volume; 
	private BigDecimal grossWt; 
	private Boolean isRTO; 
	private Long suborderId; 
	private String shippingMode; 
	private Boolean isCOD; 
	private BigDecimal payablePrice;
	private int courierId;
	
	@XmlElement
	public Long getFromPincode() {
		return fromPincode;
	}
	public void setFromPincode(Long fromPincode) {
		this.fromPincode = fromPincode;
	}
	
	@XmlElement
	public Long getToPincode() {
		return toPincode;
	}
	public void setToPincode(Long toPincode) {
		this.toPincode = toPincode;
	}
	
	@XmlElement
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	@XmlElement
	public BigDecimal getGrossWt() {
		return grossWt;
	}
	public void setGrossWt(BigDecimal grossWt) {
		this.grossWt = grossWt;
	}
	
	@XmlElement
	public Boolean getIsRTO() {
		return isRTO;
	}
	public void setIsRTO(Boolean isRTO) {
		this.isRTO = isRTO;
	}
	
	@XmlElement
	public Long getSuborderId() {
		return suborderId;
	}
	public void setSuborderId(Long suborderId) {
		this.suborderId = suborderId;
	}
	
	@XmlElement
	public String getShippingMode() {
		return shippingMode;
	}
	public void setShippingMode(String shippingMode) {
		this.shippingMode = shippingMode;
	}
	
	@XmlElement
	public Boolean getIsCOD() {
		return isCOD;
	}
	public void setIsCOD(Boolean isCOD) {
		this.isCOD = isCOD;
	}
	
	@XmlElement
	public BigDecimal getPayablePrice() {
		return payablePrice;
	}
	public void setPayablePrice(BigDecimal payablePrice) {
		this.payablePrice = payablePrice;
	}
	
	@XmlElement
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@XmlElement
	public int getCourierId() {
		return courierId;
	}
	public void setCourierId(int courierId) {
		this.courierId = courierId;
	} 
}
