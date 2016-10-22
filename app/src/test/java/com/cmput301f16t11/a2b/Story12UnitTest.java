package com.cmput301f16t11.a2b;

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story12UnitTest {

/* Based on US 03.02.01
To Test:
changing User info
*/

    @Test
    public void testChangeInfo() {
        User user = new User("user", "password", "user@ualberta.ca");
        user.setName("name1");
        assertEquals("name1", user.getName());
        user.setPassWord("7801234567");
        assertEquals("7801234567", user.getPassWord());
        user.setEmail("test@ualberta.ca");
        assertEquals("test@ualberta.ca", user.getEmail());
    }

}