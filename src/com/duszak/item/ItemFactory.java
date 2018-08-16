package com.duszak.item;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;

public class ItemFactory {
	
	private ItemFactory() {	}
	
	public static Item createItem(String typeIn, String idIn, int costIn) throws InvalidDataException, NullParameterException {
		if( typeIn.equals("standard") ) 
			return new ItemImpl(idIn, costIn);
		else
			return null;
	}
}
