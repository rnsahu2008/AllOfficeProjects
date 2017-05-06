package com.sel.casepanelTests;

import java.util.HashMap;

public enum maporderstatus {

	delivered("Order Delivered"), shipped("Order shipped"), verifed("Verified"), cargoready(
			"Cargo Ready"), returnbycustomer("Returned By Customer"), reversepickupdelivered(
			"Reverse pickup delivered"), reversepickuprequiredrefund(
			"Reverse Pickup required for Refund"), reversepickuprequiredreplacement(
			"Reverse Pickup required for Replacement"), reversepickupdone(
			"Reverse Pickup Done"), replacementinitiated(
			"Replacement Initiated"), smartcancelled("Cancelled for Refund"), smartcancelled1(
			"Cancelled"), salesreturn("Sales return"), requestforreplacement(
			"Request for replacement"), reshipped("Reshipped"),
			paymentreceived("Payment Received"),
			lostintransit("Lost in Transit"),
            refundchqdispatched("Refund Cheque Dispatched"),
            orderreturnedtovendor("Order Return to Vendor"),
            disputeraised("Dispute Raised"),
            refundchqprepared("Refund Cheque Prepared"),
            gatepassed("Gate Passed"),
            rto("RTO"),
            rtodelivered("RTO Delivered"),
            refunded("Refunded"),
            deliveryatemptfailed("DeliveryAttemptFailed"),
            ChequeReceivedOrderCommand("ChequeReceivedOrderCommand"),
	      ShortShipmentDistpatchSuborderCommand("Short shipment Dispatch"),
	      orderreturntovendoradjustment("ORTV Adjustment"),
	requestForShortShipmentSuborderCommand("Request for short shipment");
            
	;

	private String value;

	private maporderstatus(String maporderstatus) {
		this.value = maporderstatus;
	}

	private static HashMap<String, maporderstatus> ModeMap = new HashMap<String, maporderstatus>();

	static {
		for (maporderstatus Mode : maporderstatus.values()) {
			ModeMap.put(Mode.value(), Mode);
		}
	}

	public String value() {
		return this.value;
	}

	public static maporderstatus getMode(String Mode) {
		return ModeMap.get(Mode);
	}

}
