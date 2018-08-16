# SE450 Course Project

SE450 is a ten-week graduate level course. The objective of SE450 is to teach the concepts and practice of object oriented software development. The course covers advanced concepts behind object orientation including role-based programming, advanced concepts of inheritance, interface creation & usage, and design patterns. 

## Project Overview
The SE450 course project will process orders to move items (products) from one (or more) facilities to a destination facility. These items must be processed at the source facility, removed from stock and transported to the next closest facility available on an approved shipping route.

### Transportation
The project will work with a network of “facilities”. Facilities can either act as an order destination (the delivery destination for the items in an order) or an item source (a facility from which the items are taken). A “map” 18 facilities that must be represented in your application is provided.

Part of processing an order includes determining the best (i.e., shortest) path between facilities. For example, processing an order with destination “Denver” involves finding the shortest path to one of 3 facilities that have the desired item (assume they are “Chicago”, “Miami”, and “Norfolk”). While the network has many possible paths between Denver and the 3 listed facilities, the shortest paths would be:
- Chicago, IL to Denver, CO: [Chicago, IL - St. Louis, MO – Denver, CO] = 1,148 mi
- Miami, FL to Denver, CO: [Miami, FL - New Orleans, LA - Austin, TX - Santa Fe, NM - Denver, CO] = 2,456 mi
- Norfolk, VA to Denver, CO: [Norfolk, VA - Nashville, TN - St. Louis, MO - Denver, CO] = 1,866 mi

Your application must be capable of determining the shortest path between any 2 facilities. The distance between facilities should be represented as the travel time (in days):
```
Travel Time = Distance (miles) / (driving hours per day * average miles per hour)
```

### Facilities
All facilities have limited processing capabilities. The processing refers to packing items and preparing them for shipment. Facilities can process a fixed number of items per day (each facility will have its own processing rate). Once the number of items for a day has been reached at a facility, the next available day must be used to continue processing. The amount of time it will take a facility to process a request is based upon the number of items requested, the facility’s items-per-day rate, and the facility’s available days.

### Orders
An Order represents a request to have items (products) moved from one or more facilities to a destination facility. An Order should consist of the following information: ID, time, destination, list of order items.


## Processed Order Output
Output from a single processed order can be seen below.
![Project output example](https://i.imgur.com/0hpUBZA.png)

## Select Project UML Diagrams
UML diagrams for a few project packages can be seen below. 
### Loader Package
![com.duszak.loader package UML Diagram](https://raw.githubusercontent.com/paulduszak/SE450/master/src/uml/com.duszak.util.loader.png)
### Order Package
![com.duszak.order package UML Diagram](https://raw.githubusercontent.com/paulduszak/SE450/master/src/uml/com.duszak.order.png)
### Item Package
![com.duszak.item package UML Diagram](https://raw.githubusercontent.com/paulduszak/SE450/master/src/uml/com.duszak.item.png)
### Facility Package
![com.duszak.facility package UML Diagram](https://raw.githubusercontent.com/paulduszak/SE450/master/src/uml/com.duszak.facility.png)
