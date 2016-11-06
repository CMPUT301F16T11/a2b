package com.cmput301f16t11.a2b;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;


/**
 * Created by Wilky on 11/6/2016.
 */

public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {
    private Solo solo;

    public SignUpActivityTest() {
        super(SignUpActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testCheckUserTask() {
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        solo.enterText((EditText) solo.getView(R.id.activity_signup_usr), "bgwilkin");
        solo.clickOnView(solo.getView(R.id.activity_signup_pwd));
        assertTrue(solo.waitForText("Username not available"));
        solo.clearEditText((EditText)solo.getView(R.id.activity_signup_usr));

        solo.clickOnView(solo.getView(R.id.activity_signup_usr));
        solo.enterText((EditText) solo.getView(R.id.activity_signup_usr), "abcdefg");
        solo.clickOnView(solo.getView(R.id.activity_signup_pwd));
        assertTrue(solo.waitForText("Username is available"));
        solo.clearEditText((EditText)solo.getView(R.id.activity_signup_usr));

        solo.clickOnView(solo.getView(R.id.activity_signup_usr));
        solo.enterText((EditText) solo.getView(R.id.activity_signup_usr), "asdf123");
        solo.clickOnView(solo.getView(R.id.activity_signup_pwd));
        assertTrue(solo.waitForText("Username not available"));
        solo.clearEditText((EditText)solo.getView(R.id.activity_signup_usr));

        solo.clickOnView(solo.getView(R.id.activity_signup_usr));
        solo.enterText((EditText) solo.getView(R.id.activity_signup_usr), "12345678");
        solo.clickOnView(solo.getView(R.id.activity_signup_pwd));
        assertTrue(solo.waitForText("Username is available"));
        solo.clearEditText((EditText)solo.getView(R.id.activity_signup_usr));

        solo.clickOnView(solo.getView(R.id.activity_signup_usr));
        solo.enterText((EditText) solo.getView(R.id.activity_signup_usr), "");
        solo.clickOnView(solo.getView(R.id.activity_signup_pwd));
        assertTrue(solo.waitForText("Please enter a username"));

    }

    public void testPasswordStrength() {
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwd), "abcdefgh");
        assertFalse(getActivity().strongPwd);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_pwd));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwd), "Str0nger");
        assertTrue(getActivity().strongPwd);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_pwd));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwd), "weakbuffer");
        assertFalse(getActivity().strongPwd);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_pwd));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwd), "Str0ng3st");
        assertTrue(getActivity().strongPwd);
    }

    public void testMatchingPassword() {
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwd), "TestPa55");

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwdConf), "TestPass");
        assertFalse(getActivity().matchPwd);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_pwdConf));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwdConf), "TestPa55");
        assertTrue(getActivity().matchPwd);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_pwdConf));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_pwdConf), "Wrong again");
        assertFalse(getActivity().matchPwd);
    }

    public void testEmail() {
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        solo.enterText((EditText) solo.getView(R.id.activity_signup_email), "testing@testing.com");
        assertTrue(getActivity().properEmail);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_email));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_email), "@.ca");
        assertFalse(getActivity().properEmail);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_email));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_email), "real@email.ca");
        assertTrue(getActivity().properEmail);
    }

    public void testPhone() {
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        assertFalse(getActivity().properPhone);

        solo.enterText((EditText) solo.getView(R.id.activity_signup_phone), "1234567890");
        assertTrue(getActivity().properPhone);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_phone));

        solo.enterText((EditText) solo.getView(R.id.activity_signup_phone), "123456789");
        assertFalse(getActivity().properPhone);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_phone));


        solo.enterText((EditText) solo.getView(R.id.activity_signup_phone), "123-456-7890");
        assertTrue(getActivity().properPhone);
        solo.clearEditText((EditText) solo.getView(R.id.activity_signup_phone));
    }

    public void testAddUserTask() {
        String testData = "test";
        User testUser = new User(testData, testData, testData, testData);

        ElasticsearchUserController.AddUserTask addUser = new ElasticsearchUserController.AddUserTask();
        Boolean result;

        addUser.execute(testUser);
        try {
            result = addUser.get();
            assertTrue(result);
        } catch (Exception e) {
            Log.i("Error", "Failed to execute asynctask");
        }

    }

}
