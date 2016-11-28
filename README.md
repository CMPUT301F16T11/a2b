Welcome to a2b, a CMPUT301 project! 


** Please Note **
Older Android emulators have problems sending location. While the app 
run fine on the emulator, the current location may not work. We        
recommend using the app on emulator sdk 24 and above. The assignment
required functionality will run on 18 and above, however for the full experience we recommend 24 and above.           

If you want details on this issue and how to get it working on older APIs, 
this guide details it: 
http://stackoverflow.com/questions/19372399/running-google-map-application-on-android-emulator

Running on lab machines:
Please note our maps api uses google play services so please install the newest version of google play 
service from sdk manager, it is under extras. After this point you must create an emulator at least of
api 18 that has google api(or else the map wont render). Here are some simple tips to get our project 
running on emulator in the lab machine. After you have this emulator set up please change the jdk path
to: /usr/lib/jvm/java-8-oracle. This is done by going file-> Project Structure-> Sdk location-> Jdk path and 
then changing that path to /usr/lib/jvm/java-8-oracle.

USE CASE VIDEO:
	https://youtu.be/Ac_TLTTdzjc
COMMERCIAL VIDEO:
	https://youtu.be/RAOcT5V4Ml8


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
	confirming a driver, accepting a request, completing a request and submitting payment
	are avaiable based on the ownership and state of the request. 

View Profile: 
	Here profile information of a user is shown. If you are logged into the account you are 
	currently viewing, you can edit the email and phone number associated with your account. 
	If you are viewing another users profile, you can tap their phone number or email to call
	or email them. 

While Offline: 
	You can view the map in rider mode in your current locale (currently Edmonton only). 
	You can set requests to automatically be sent when you reconnect to the internet. 
	Please select "update data" in the three-dot menu in the top right corner to force an update. 
	In driver mode, you can view requests you've already accepted and accept new ones to be updated
	when you are online again (by the same procedure). 



For more information, documentation and examples, please see the wiki!



Licensing Details

Reuse Statement:

	Licensed under Apache License, Version 2.0, hereby known as "license:" (view a copy in file "LICENSE")
	by CMPUT301F16T11.
	You may not use this project except in compliance with the license. You may also obtain a copy of this
	license http://www.apache.org/licenses/LICENSE-2.0. 
	Unless required by applicable law, software distributed under this license is distributed under 
	an "as is" basis. NO WARRANTY, CONDITIONS, expressed or implied are provided. 
	All third party libraries as well as attributions are used as per their reuse statements and licenses, 
	which are compatible with this resuse statement and description. Please see third party libraries 
	in this file and doc/licensing/license_list for more details. 
	See the license for more details. 

Third Party Attributions
	
	Many examples, solutions code samples and tutorials were used in the production of this project.
	They are all used under licenses compatible with Apache 2.0. 
	The headers of all source code files containing the aforementioned work or derivatives thereof 
	state the usage with all necessary attributions and licensing details. 
	These details are also collected and described in doc/licensing/license_list.txt
	Full copies of compatible licenses used in this project are available in doc/licensing. 

Third Party Libraries
	Here are the (additional to android) third party libraries that were used and their
	reuse statements. 

	GSON https://github.com/google/gson 
	Apache 2.0
	Copyright 2008 Google Inc.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

	JestDroid https://github.com/searchbox-io/Jest/tree/master/jest-droid
	Apache 2.0 
	Reuse Satement: 
	Copyright 2013 www.searchly.com
	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:
	http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

	Android GMAPS Addons
	Copyright (c) 2014 Jonathan Baker https://github.com/cocoahero/android-gmaps-addons
	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

	Google Maps API
	Copyright (c) 2016 Google Inc.
	https://developers.google.com/maps/
	(Online Only)
	Google Maps Android API is used under the following fair use guidelines:
	Apart from any license granted to you by Google, your use of the Content may be acceptable under principles of "fair use." Fair use is a concept under copyright law in the U.S. that, generally speaking, permits you to use a copyrighted work in certain ways without obtaining a license from the copyright holder.
	There are similar, although generally more limited, concepts in other countries' copyright laws, including a concept known as "fair dealing" in a number of countries. Google can’t tell you if your use of the Content from our products would be fair use or would be considered fair dealing; these are legal analyses that depend on all of the specific facts of your proposed use. We suggest you speak with an attorney if you have questions regarding fair use of copyrighted works.

	OpenStreetMap Tiles
	© OpenStreetMap contributors
	http://www.openstreetmap.org/#map=5/51.495/-0.088
	(Offline Only)
	This data is made available over the Open Database License (available at doc/licensing/OpenData.txt orhttp://opendatacommons.org/licenses/odbl/) 
	The cartography of the map tiles is licensed over CC-BY-SA 2.0 (available at 
	in doc/licensing/CreativeCommons.txt or https://creativecommons.org/licenses/by-sa/2.0/?). 

	Design patterns:
	Singleton
		- UserController: One static instance of User that gets set on sign in is shared by all
		activities.

	Builder pattern
		- Jests Builder classes are used to build commands that are executed by the JEST client
		- When app is in offline mode, instructions are created and and put on a command stack 
		that is to be executed when connectivity has been regained. This updates updates the
		info on the server.
		-RiderNotificationService and DriverNotificaton system utilize the Android notification builder 

	Templepate pattern
		- App takes advantage of the hooks provided by Activity class 

	Adapter pattern
		- Addapted Offline Requests to Online Requests
	
	Proxy pattern
		- Used the proxy pattern for representing full UserRequests when the user was not online.
		Full UserRequests were substitued in when the user came back online and more information 
		was avaiable. 

	State pattern
		- User held state information, whether a user was a currently a driver or a rider. 
		- Request held state information, whether a user was unaccepted, accepted, confiremed, i
		nprogress, completed, payment recived.
		- App held state informatoion, whether or not it was to operate in offline or online mode.

	Decorator pattern
		- ShadedListAdapter makes use of a decorator pattern

	Observer pattern
		- Most activities have click listeners which are observers that are observing 
		various UI elements	


For more information, documentation and examples, please see the wiki!


