package com.duszak.item;

import java.text.NumberFormat;
import java.util.Locale;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;

public class ItemImpl implements Item {
	
	private String itemId;
	private float cost;
	
	public ItemImpl(String idIn, float costIn) throws InvalidDataException, NullParameterException {
		setItemId(idIn);
		setCost(costIn);
	}
	
	public String getItemId()  {
		return itemId;
	}

	public float getCost() {
		return cost;
	}
	
	private void setItemId(String itemIdIn) throws NullParameterException, InvalidDataException {
		if (itemIdIn == null)
			throw new NullParameterException("Item ID cannot be null.");
		if (itemIdIn.length() <= 0)
			throw new InvalidDataException("Item ID cannot be empty string.");
		
		itemId = itemIdIn;
	}
	
	private void setCost(float costIn) throws InvalidDataException {
		if (costIn < 0)
			throw new InvalidDataException("Item 'cost' cannot be negative.");
		
		cost = costIn;
	}
		
	@Override
	public String toString() {
		return String.format("%10s : $%s", getItemId(), NumberFormat.getNumberInstance(Locale.US).format(getCost()));
	}

}
