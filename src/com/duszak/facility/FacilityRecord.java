package com.duszak.facility;

public class FacilityRecord implements Comparable<FacilityRecord>{
	
	public String facilityId;
	public int requestedQuantity;
	public int processingEndDay;
	public float travelTime;
	public int arrivalDay;
	public float cost;
	
	public FacilityRecord (String facilityIdIn, int requestedQuantityIn, int processingEndDayIn, float travelTimeIn, int arrivalDayIn) {
		facilityId = facilityIdIn;
		requestedQuantity = requestedQuantityIn;
		processingEndDay = processingEndDayIn;
		travelTime = travelTimeIn;
		arrivalDay = arrivalDayIn;
	}

	@Override
	public int compareTo(FacilityRecord fR) {
		return arrivalDay - fR.arrivalDay;
	}

	public void calculateCost(float itemCost, float facilityCost, int shippingCost) {
		cost = itemCost + facilityCost + shippingCost;
	}
}
