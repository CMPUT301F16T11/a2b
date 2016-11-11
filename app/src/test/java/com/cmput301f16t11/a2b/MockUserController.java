package com.cmput301f16t11.a2b;

/**
 * Created by tothd on 11/10/2016.
 */

public class MockUserController extends UserController {
    private static MockUser mockUser;
    public MockUserController(User u) {
        super(u);
    }

    public static MockUser getMockUser(){
        return mockUser;
    }
}
