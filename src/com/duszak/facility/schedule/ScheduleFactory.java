package com.duszak.facility.schedule;

public class ScheduleFactory {
	
	private ScheduleFactory() {	}
	
	public static Schedule createSchedule(String typeIn, int rateIn) {
		if( typeIn.equals("basic") ) 
			return new ScheduleImpl(rateIn);
		else
			return null;
	}
}
