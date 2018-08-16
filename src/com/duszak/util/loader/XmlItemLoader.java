package com.duszak.util.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.duszak.exception.InvalidDataException;
import com.duszak.exception.NullParameterException;
import com.duszak.item.ItemFactory;

public class XmlItemLoader implements Loader {
	
	private final String baseElement = "Item";

	@Override
	public List<Object> load(String path) {
		List<Object> items = new ArrayList<Object>();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	
	        File xml = new File(path);
	        if (!xml.exists()) {
	            System.err.println("**** XML File '" + path + "' cannot be found");
	            System.exit(-1);
	        }
	
	        Document doc = db.parse(xml);
	        doc.getDocumentElement().normalize();
	
	        NodeList entries = doc.getDocumentElement().getChildNodes();
	
	        for (int i = 0; i < entries.getLength(); i++) {
	        	
	            String id;
	            int cost;

	            if (entries.item(i).getNodeType() == Node.TEXT_NODE) {
	                continue;
	            }
	            
	            String entryName = entries.item(i).getNodeName();
	            if (!entryName.equals(baseElement)) {
	                System.err.println("Unexpected node found: " + entryName);
	                System.exit(-1);
	            }
	            
	            Element elem = (Element) entries.item(i);
	            
	            id = elem.getElementsByTagName("Id").item(0).getTextContent();
	            cost = Integer.parseInt(elem.getElementsByTagName("Cost").item(0).getTextContent());	            
	            
				try {
					items.add(ItemFactory.createItem("standard", id, cost));
				} catch (InvalidDataException e) {
					System.err.println("Current item: " + id);
					e.printStackTrace();
					System.exit(1);
				} catch (NullParameterException e) {
					System.err.println("Current item: " + id);
					e.printStackTrace();
					System.exit(1);
				}
	        }

		} catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
	        e.printStackTrace();
	        return null;
	    }
		
		return items;
	}

}
