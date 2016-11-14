package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Created by tothd on 11/10/2016.
 * Not implemented, perhaps used for part 5
 */

public class MockRequestController extends RequestController {
    private static ArrayList<MockUserRequest> saveData;
    private static MockUser mockUser;
    public MockRequestController() {
        saveData = new ArrayList<>();
    }
    public static void addOpenRequest(MockUserRequest request) {
        //save request to a file or array
        saveData.add(request);

    }

}
