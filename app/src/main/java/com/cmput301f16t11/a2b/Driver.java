package com.cmput301f16t11.a2b;

import java.util.ArrayList;

public class Driver extends User {
	int rating;
	String name;
	ArrayList<UserRequest> Acceptedrequests;

	Driver(String name, int rating) {
		this.rating = rating;
		this.name = name;
		Acceptedrequests = new ArrayList<UserRequest>();
	}

	public int getRating() {
		return rating;
	}

	public void acceptRequest(UserRequest request) {
		Acceptedrequests.add(request);
	}

	public boolean hasAccepted(UserRequest request) {
		if(Acceptedrequests.contains(request)) {
			return true;
		} else {return false;

		}
	}
}