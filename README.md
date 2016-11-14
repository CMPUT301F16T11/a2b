Welcome to a2b, a CMPUT301 project! 

Installation is simple, just install the apk file like any other android app!

Running on lab machines:
Please note our maps api uses google play services so please install the newest version of google play 
service from sdk manager, it is under extras. After this point you must create an emulator at least of
api 18 that has google api(or else the map wont render). Here are some simple tips to get our project 
running on emulator in the lab machine. After you have this emulator set up please change the jdk path
to: /usr/lib/jvm/java-8-oracle. This is done by going file-> Project Structure-> Sdk location-> Jdk path and 
then changing that path to /usr/lib/jvm/java-8-oracle.

Licensing stuff: 
	This project is licensed under Apache 2.0. Please see the LICENSE file in the main dir
	for details. 

	3rd Party licensing is also used. Please see doc/licensing for details on work that
	has been licensed in the production of this project. These include links to relevant
	materials and the date these materials were accessed. 

Permissions required: 
	Location 

If you don't have an account click sign up when you load the app. Otherwise simply enter your 
username and hit sign in. The app will automatically load into a map view and center on your 
current location. You'll notice by the top of the screen that you're in rider mode. 

Rider mode: 
	You can make ride requests by choosing (or searching) two separate places on the map 
	and using the button in the bottom right corner to set them and initiate a request. 

	A suggested fair fare will already be filled out for you, but feel free to change this. 
	You can view requests you've made (and various other categories of requests) by clicking 
	on the three dots menu in the top right corner and hitting View Requests. You can 
	also view/edit your profile and change to driver mode in this menu

Driver Mode:
	You can select a search radius, search for new requests and also view/accept requests from 
	the map visually or in the same view requests view as there is for riders. 

Request Details:
	When you've selected a request from the request details page you can view it's current status, 
	confirmed driver (if any), the rider, the start and end locations (click them to view on map)
	and a list of drivers who've accepted the request. From here various actions like payment, 
	confirming a driver, accepting a request, completing a request are avaiable based on the 
	ownership and state of the request. 

View Profile: 
	Here profile information of a user is shown. If you are logged into the account you are 
	currently viewing, you can edit the email and phone number associated with your account. 
	If you are viewing another users profile, you can tap their phone number or email to call
	or email them. 

For more information, documentation and examples, please see the wiki!
