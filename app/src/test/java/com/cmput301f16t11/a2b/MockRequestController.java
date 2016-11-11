package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Created by tothd on 11/10/2016.
 */

public class MockRequestController extends RequestController {
    
    public static void addOpenRequest(MockUserRequest request) {
        //save request to a file or array
        ArrayList<MockUserRequest> file = new ArrayList<MockUserRequest>();
        file.add(request);

    }
}
