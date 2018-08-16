package com.duszak.order;

import java.util.Map;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;

public class OrderFactory {

	private OrderFactory() { }
	
	public static Order createOrder(String type, String id, int time, String destination, Map<String, Integer> items) throws NullParameterException, InvalidDataException {
		if( type.equals("standard") ) 
			return new OrderImpl(id, time, destination, items);
		else
			return null;
	}
}
