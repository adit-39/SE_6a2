# SE_6a2: Vehicle Fleet Management System

## Overview:
This Fleet Management System (FMS) is made up of the Following components:

* An **Algorithmic Component (Logic Server)** to find the best routes for a vehicle given addresses of all the passengers and details of all vehicles in the fleet. Costs are calculated and fee amounts for each passenger is set according to their distance from the organization [One time operation].

* An **Android Application** for the passengers of the transport service through which they can be tracked just before entry and just after exit from the vehicles to raise notifications to them regarding arrival of the vehicle at the destination and also to inform loved ones of entry/exit from the vehicle. Passengers may provide feedback to the administrators as well. Another application for the parent to receive notifications.

* An **Administrator Interface** to provide for realtime tracking of passengers and buses for a "big picture" and also to look into feedback from the passengers of buses. A small number of other admin features may also be included.

* An **API Layer** to handle requests from various clients and authorize access to certain sections of the database and to glue together the various components of the system and coordinate their activities.

* A **Database Server** to purely deal with data entry into and reading from the database.

* An add-on in the form of an **Algorithm Visualizer** to walk one through the procedure that the algorithm performs to build routes.

## Code:
All the above components, except the database server, are organized into their respective code folders.

## Status:
* *Algorithm*: 100% complete (currently in the clustering of geolocations)
* *Android App*: 100% complete (UI, forms for feedback, push notifications need to be completed)
* *Administrator Interface*: 95% complete (Awaiting all APIs to integrate with database for location reading)
* *API Layer*: 100% complete (Location fetch from database needs to be completed)
* *Database*: 100% complete (Student and driver information to be added for persistent storage)
* *Visualizer*: 100% complete (Currently plotting in a basic manner)
