package com.example.liangkelvin.a2btests

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story14UnitTest {

/* Based on US 04.01.01
   Billy is in a great mood. It's been a year and a half since 
   he started driving for a2b. Now he and his fiance are looking 
   for a property with the help of the wonderful real estate agent 
   who often uses a2b to provide transport for customers. With a 
   content sigh he pulls out the root of his happiness, his a2b app. 
   He decides to search for any requests in his area by clicking the 
   "target" button to load his current location. He finds one nearby 
   and heads out. Billy is an excellent example of a driver making an 
   average request.

To Test:
finding requests
*/

	User user;
	String userName = "a2bBringsMeHappiness";
	String passWord = "allINeedIsa2b";


	@Test
	public void testGetRequests() {
		Request request = new Request("10025 125 st", "12586 58 ave", 123);
		ArrayList<Request> requests = new ArrayList<Request>();
		requests.add(request);
		Request request2 = new Request("1212 32 st", "12322 43 ave", 23);
		requests.add(request2);
		// TODO: have requests added to some over arching class
		user = UserController.getUser(userName);
		ArrayList<Request> retrieve = user.getRequests(myLocation);
		assertEquals(retrieve(0), request);
		assertEquals(retrieve(1), request2);
	}