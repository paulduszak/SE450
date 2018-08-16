package com.duszak.util.loader;

public class LoaderFactory {
	
	private LoaderFactory() { }
	
	public static Loader createLoader(String type) {
		
		if ( type.equals("facility") ) {
			return new XmlFacilityLoader();
		} else if ( type.equals("item") ) {
			return new XmlItemLoader();
		} else if ( type.equals("order") ) {
			return new XmlOrderLoader();
		}
		return null;
	}
}
