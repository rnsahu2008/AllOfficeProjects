package com.hs18.DataUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.hs18.util.LocatorExcel;

@XmlRootElement
public class BulkOrderCreationOrderCommand extends LocatorExcel {

	private String userId;
	private String userLogin;
	private String buyerFirstName;
	private String buyerLastName;
	private String billingAddress1;
	private String billingCity;
	private String billingZip;
	private String billingCountry;
	private String billingMobile;
	private String billingEmail;
	private String receiverFirstName;
	private String receiverLastName;
	private String shippingAddress1;
	private String receivingCity;
	private String receivingCountry;
	private String receivingzip;
	private String receivingMobile;
	private String remark;
	private String itemCode;
	private String quantity;
	private String catalogueRFNum;
	private String ccRfNum;
	private String isCallCenter;
	private String suborderStatus;
	private String paymentMode;
	private String orderId;
	private String gcNumber;
	private String commandId;
	private String callerNumber;
	private String route;
	private String dthRfNum;
	private String leadSource;
	

	@XmlElement
	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	@XmlElement
	public String getBuyerFirstName() {
		return buyerFirstName;
	}

	public void setBuyerFirstName(String buyerFirstName) {
		this.buyerFirstName = buyerFirstName;
	}

	@XmlElement
	public String getBuyerLastName() {
		return buyerLastName;
	}

	public void setBuyerLastName(String buyerLastName) {
		this.buyerLastName = buyerLastName;
	}

	@XmlElement
	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	@XmlElement
	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	@XmlElement
	public String getBillingZip() {
		return billingZip;
	}

	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	@XmlElement
	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	@XmlElement
	public String getBillingMobile() {
		return billingMobile;
	}

	public void setBillingMobile(String billingMobile) {
		this.billingMobile = billingMobile;
	}

	@XmlElement
	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	@XmlElement
	public String getReceiverFirstName() {
		return receiverFirstName;
	}

	public void setReceiverFirstName(String receiverFirstName) {
		this.receiverFirstName = receiverFirstName;
	}

	@XmlElement
	public String getReceiverLastName() {
		return receiverLastName;
	}

	public void setReceiverLastName(String receiverLastName) {
		this.receiverLastName = receiverLastName;
	}

	@XmlElement
	public String getShippingAddress1() {
		return shippingAddress1;
	}

	public void setShippingAddress1(String shippingAddress1) {
		this.shippingAddress1 = shippingAddress1;
	}

	@XmlElement
	public String getReceivingCity() {
		return receivingCity;
	}

	public void setReceivingCity(String receivingCity) {
		this.receivingCity = receivingCity;
	}

	@XmlElement
	public String getReceivingCountry() {
		return receivingCountry;
	}

	public void setReceivingCountry(String receivingCountry) {
		this.receivingCountry = receivingCountry;
	}

	@XmlElement
	public String getReceivingzip() {
		return receivingzip;
	}

	public void setReceivingzip(String receivingzip) {
		this.receivingzip = receivingzip;
	}

	@XmlElement
	public String getReceivingMobile() {
		return receivingMobile;
	}

	public void setReceivingMobile(String receivingMobile) {
		this.receivingMobile = receivingMobile;
	}

	@XmlElement
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@XmlElement
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@XmlElement
	public String getCatalogueRFNum() {
		return catalogueRFNum;
	}

	public void setCatalogueRFNum(String catalogueRFNum) {
		this.catalogueRFNum = catalogueRFNum;
	}

	@XmlElement
	public String getCcRfNum() {
		return ccRfNum;
	}

	public void setCcRfNum(String ccRfNum) {
		this.ccRfNum = ccRfNum;
	}

	@XmlElement
	public String getIsCallCenter() {
		return isCallCenter;
	}

	public void setIsCallCenter(String isCallCenter) {
		this.isCallCenter = isCallCenter;
	}

	@XmlElement
	public String getSuborderStatus() {
		return suborderStatus;
	}

	public void setSuborderStatus(String suborderStatus) {
		this.suborderStatus = suborderStatus;
	}

	@XmlElement
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	@XmlElement
	public String getGcNumber() {
		return gcNumber;
	}

	public void setGcNumber(String gcNumber) {
		this.gcNumber = gcNumber;
	}

	@XmlElement
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@XmlElement
	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	@XmlElement
	public String getCallerNumber() {
		return callerNumber;
	}

	public void setCallerNumber(String callerNumber) {
		this.callerNumber = callerNumber;
	}
	@XmlElement
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	@XmlElement
	public String getDthRfNum() {
		return dthRfNum;
	}

	public void setDthRfNum(String dthRfNum) {
		this.dthRfNum = dthRfNum;
	}
	@XmlElement
	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

}
