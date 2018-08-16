package com.duszak.util.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.duszak.order.OrderFactory;

public class XmlOrderLoader implements Loader {

	private final String baseElement = "Order"; //element(s) to be extracted from XML

	@Override
	public List<Object> load(String path) {
		List<Object> orders = new ArrayList<Object>();
		
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
	        	
	            String orderId, destination;
	            int time;
	            Map<String, Integer> items = new HashMap<String, Integer>();
	        	
	            if (entries.item(i).getNodeType() == Node.TEXT_NODE) {
	                continue;
	            }
	            
	            String entryName = entries.item(i).getNodeName();
	            if (!entryName.equals(baseElement)) {
	                System.err.println("Unexpected node found: " + entryName);
	                System.exit(-1);
	            }
	            
	            Element elem = (Element) entries.item(i);
	            
	            orderId = elem.getElementsByTagName("Id").item(0).getTextContent();
	            destination = elem.getElementsByTagName("Destination").item(0).getTextContent();

	            time = Integer.parseInt(elem.getElementsByTagName("Time").item(0).getTextContent());

				NodeList itemList = elem.getElementsByTagName("Item");
				
				for (int j=0; j < itemList.getLength(); j++) {
					if (itemList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = itemList.item(j).getNodeName();
                    if (!entryName.equals("Item")) {
                        System.err.println("Unexpected node found: " + entryName);
                        System.exit(-1);
                    }

                    elem = (Element) itemList.item(j);
                    String id = elem.getElementsByTagName("id").item(0).getTextContent();
                    int quantity = Integer.parseInt(elem.getElementsByTagName("quantity").item(0).getTextContent());
                   
					items.put(id, quantity);
				}
				
				try {
					orders.add(OrderFactory.createOrder("standard", orderId, time, destination, items));
				} catch (NullParameterException e) {
					System.err.println("Current order: " + orderId);
					e.printStackTrace();
					System.exit(1);
				} catch (InvalidDataException e) {
					System.err.println("Current order: " + orderId);
					e.printStackTrace();
					System.exit(1);
				}
	        }

		} catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
	        e.printStackTrace();
	        return null;
	    }
		return orders;
	}
}
