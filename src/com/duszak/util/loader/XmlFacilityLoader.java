package com.duszak.util.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.duszak.facility.FacilityFactory;
import com.duszak.facility.inventory.Inventory;

public class XmlFacilityLoader implements Loader {
	
	private final String baseElement = "Facility"; //element(s) to be extracted from XML

	@Override
	public List<Object> load(String path) {
		List<Object> facilities = new ArrayList<Object>();
		
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
	        	
	            String name;
	            int rate, cost;
	            Map<String, Inventory> inventory = new TreeMap<String, Inventory>();
	            Map<String, Integer> neighbors = new TreeMap<String, Integer>();
	        	
	            if (entries.item(i).getNodeType() == Node.TEXT_NODE) {
	                continue;
	            }
	            
	            String entryName = entries.item(i).getNodeName();
	            if (!entryName.equals(baseElement)) {
	                System.err.println("Unexpected node found: " + entryName);
	                System.exit(-1);
	            }
	            
	            Element elem = (Element) entries.item(i);
	            
	            name = elem.getElementsByTagName("Name").item(0).getTextContent();
	            rate = Integer.parseInt(elem.getElementsByTagName("ProcessRate").item(0).getTextContent());
	            cost = Integer.parseInt(elem.getElementsByTagName("Cost").item(0).getTextContent());	            
	            
				NodeList neighborList = elem.getElementsByTagName("Neighbor");
				NodeList inventoryList = elem.getElementsByTagName("Item");
				
				for (int j=0; j < neighborList.getLength(); j++) {
					if (neighborList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = neighborList.item(j).getNodeName();
                    if (!entryName.equals("Neighbor")) {
                        System.err.println("Unexpected node found: " + entryName);
                        System.exit(-1);
                    }

                    elem = (Element) neighborList.item(j);
                    neighbors.put(elem.getElementsByTagName("Name").item(0).getTextContent(), Integer.parseInt(elem.getElementsByTagName("Distance").item(0).getTextContent()));
				}
	            
				for (int k=0; k < inventoryList.getLength(); k++) {
					if (inventoryList.item(k).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = inventoryList.item(k).getNodeName();
                    if (!entryName.equals("Item")) {
                        System.err.println("Unexpected node found: " + entryName);
                        System.exit(-1);
                    }

                    elem = (Element) inventoryList.item(k);
                    String id = elem.getElementsByTagName("id").item(0).getTextContent();
                    int quantity = Integer.parseInt(elem.getElementsByTagName("quantity").item(0).getTextContent());
                    try {
						inventory.put(id, new Inventory(id, quantity));
					} catch (NullParameterException e) {
						System.err.println("Current facility: " + name);
						e.printStackTrace();
						System.exit(1);
					} catch (InvalidDataException e) {
						System.err.println("Current facility: " + name);
						e.printStackTrace();
						System.exit(1);
					}
				}
				
				try {
					facilities.add(FacilityFactory.createFacility("standard", name, rate, cost, neighbors, inventory));
				} catch (NullParameterException e) {
					System.err.println("Current facility: " + name);
					e.printStackTrace();
					System.exit(1);
				} catch (InvalidDataException e) {
					System.err.println("Current facility: " + name);
					e.printStackTrace();
					System.exit(1);
				}
	        }

		} catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
	        e.printStackTrace();
	        return null;
	    }
		return facilities;
	}

}
