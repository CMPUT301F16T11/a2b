package com.cmput301f16t11.a2b;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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

        user = UserController.loadUser(userName);
        UserRequest request = new UserRequest("10025 125 st", "12586 58 ave", 123);
        UserRequest request2 = new UserRequest("1212 32 st", "12322 43 ave", 23);
        user.addRequest(request);
        user.addRequest(request2);

        ArrayList<UserRequest> retrieve = user.getRequests();
		assertEquals(retrieve.get(0), request);
		assertEquals(retrieve.get(1), request2);
	}

}