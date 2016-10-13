package com.cmput301f16t11.a2b;

import org.junit.Test;
import java.net.Authenticator;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class Story14UnitTest {

/* Based on US 04.01.01

To Test:
finding requests
*/

	User user;
	String userName = "a2bBringsMeHappiness";
	String passWord = "allINeedIsa2b";
	String myLocation = "nowhere";


	@Test
	public void testGetRequests() {
		UserRequest request = new UserRequest("10025 125 st", "12586 58 ave", 123);
		ArrayList<UserRequest> requests = new ArrayList<UserRequest>();
		requests.add(request);
		UserRequest request2 = new UserRequest("1212 32 st", "12322 43 ave", 23);
		requests.add(request2);
		// TODO: have requests added to some over arching class
		user = UserController.loadUser(userName);
		ArrayList<UserRequest> retrieve = user.getRequests(myLocation);
		assertEquals(retrieve.get(0), request);
		assertEquals(retrieve.get(1), request2);
	}

}