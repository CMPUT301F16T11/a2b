package com.cmput301f16t11.a2b;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by cfs on 11/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class AcceptingUnitTestMocked {

    private static LatLng startLocation = new LatLng(50,50);
    private static LatLng endLocation = new LatLng(50,50);
    private static Number fare = 10.00;
    @Mock private User mUser;
    //private UserRequest mRequest;
    @Spy UserRequest mRequest = new UserRequest(startLocation,endLocation,fare,mUser);

    @Before
    public void setupTest(){
        mUser = new User();
        //@Spy UserRequest mRequest = new UserRequest(startLocation,endLocation,fare,mUser);
    }

    @Mock
    Context context;

    @Test
    public void testDriverAcceptingRequest() {

        // random request that Billy wants to accept
        //MockUserRequest billyRequest = new MockUserRequest(startLocation, endLocation, fare, driver);
//        when(context.getApplicationContext()).thenReturn(context);
//        request = new UserRequest(context);
//        assertTrue(user.getActiveRequestsAsDriver().size() > 0);
//        user.addActiveDriverRequest(request);
//        assertTrue(user.hasAcceptedRequests(request));
    }
}
