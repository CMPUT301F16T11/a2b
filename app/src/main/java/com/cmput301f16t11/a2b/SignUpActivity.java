package com.cmput301f16t11.a2b;

import android.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *  Activity that prompts user to fill out required information.
 *
 *  Checks for unique username & a strong password before creating user.
 */
public class SignUpActivity extends AppCompatActivity {

    // Views from activity_sign_up.xml
    private TextView errorMsg;
    private EditText usr;
    private ImageView usrIcon;
    private TextView usrMsg;
    private EditText pwd;
    private EditText pwdConf;
    private ImageView pwdIcon;
    private ArrayList<TextView> pwdStrength = new ArrayList<TextView>();;
    private TextView pwdMsg;
    private EditText email;
    private EditText phone;
    private Button signupButton;


    // Sign up data test variables
    protected Boolean uniqueUsr = false;
    protected Boolean matchPwd = false;
    protected Boolean strongPwd = false;
    protected Boolean properEmail = false;
    protected Boolean properPhone = false;
    protected Boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Find all UI views
        assignViews();

        // Assign Click Listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Case where all criteria are met
                if (uniqueUsr & matchPwd & strongPwd & properEmail & properPhone) {
                    // Remove errorMsg if necessary
                    if (error) {
                        errorMsg.setText("");
                        errorMsg.setVisibility(View.GONE);
                        error = false;
                    }

                    ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
                    User newUser = createNewUser();

                    // Failed to create password hash
                    if (newUser==null) {
                        error = true;
                        errorMsg.setText(R.string.signup_creation_error);
                        errorMsg.setVisibility(View.VISIBLE);
                    }
                    // Attempt to add user
                    else {
                        addUserTask.execute(newUser);
                        try {
                            Boolean result = addUserTask.get();

                            if (result) {
                                //TODO: Launch next activity after user creation (MainActivity?)

                            } else {
                                // Failed to add new user
                                error = true;
                                errorMsg.setText(R.string.signup_creation_error);
                                errorMsg.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.i("Error", "AsyncTask failed to execute");
                            e.printStackTrace();
                        }
                    }
                }
                // Not all criteria met -- display required error messages
                else {
                    error = true;
                    displayErrorStr();
                }

            }
        });

        // Assign TextWatcher to determine if criteria is met
        pwd.addTextChangedListener(new TextWatcher() {
            int last_state = 0;
            Pattern good = Pattern.compile(".*[^A-Za-z].*");
            Pattern strong = Pattern.compile(".*[^A-Za-z].*[^A-Za-z].*");

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currPwd = pwd.getText().toString();
                // Strong password has 2 numbers/special characters
                if (currPwd.length()>=8 && strong.matcher(currPwd).matches()) {
                    if (last_state==2) {
                        pwdStrength.get(2).setBackgroundResource(R.color.signup_strong_on);
                    }
                    else if (last_state==1) {
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_on);
                        pwdStrength.get(2).setBackgroundResource(R.color.signup_strong_on);
                        pwdMsg.setText("");
                        strongPwd = true;
                    }
                    else if (last_state==0) {
                        pwdStrength.get(0).setBackgroundResource(R.color.signup_weak_on);
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_on);
                        pwdStrength.get(2).setBackgroundResource(R.color.signup_strong_on);
                        pwdMsg.setText("");
                        strongPwd = true;
                    }
                    last_state=3;
                }
                // Acceptable password contains 1 special character
                else if (currPwd.length()>=8 && good.matcher(currPwd).matches()) {
                    if (last_state==3) {
                        pwdStrength.get(2).setBackgroundResource(R.color.signup_strong_off);
                    }

                    else if (last_state==1) {
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_on);
                        pwdMsg.setText("");
                        strongPwd = true;
                    }

                    else if (last_state==0) {
                        pwdStrength.get(0).setBackgroundResource(R.color.signup_weak_on);
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_on);
                        pwdMsg.setText("");
                        strongPwd = true;
                    }
                    last_state=2;
                }
                // Weak password is long enough without any special characters
                else if (currPwd.length()>=8) {
                    if (last_state==0) {
                        pwdStrength.get(0).setBackgroundResource(R.color.signup_weak_on);
                        pwdMsg.setText(R.string.signup_pwd_specError);
                    }
                    else if (last_state==2) {
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_off);
                        pwdMsg.setText(R.string.signup_pwd_specError);
                        strongPwd = false;
                    }
                    last_state=1;
                }
                // Password is less than 8 characters
                else {
                    if (last_state==3) {
                        pwdStrength.get(0).setBackgroundResource(R.color.signup_weak_off);
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_off);
                        pwdStrength.get(2).setBackgroundResource(R.color.signup_strong_off);
                        pwdMsg.setText(R.string.signup_pwd_lenError);
                        strongPwd = false;
                    }
                    else if (last_state==2) {
                        pwdStrength.get(0).setBackgroundResource(R.color.signup_weak_off);
                        pwdStrength.get(1).setBackgroundResource(R.color.signup_good_off);
                        pwdMsg.setText(R.string.signup_pwd_lenError);
                        strongPwd = false;
                    } else if (last_state==1){
                        pwdStrength.get(0).setBackgroundResource(R.color.signup_weak_off);
                        pwdMsg.setText(R.string.signup_pwd_lenError);
                    }
                    last_state=0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwdConf.addTextChangedListener(new TextWatcher() {
            String pwd1;
            String pwd2;
            int last_state = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwd1 = pwd.getText().toString();
                pwd2 = pwdConf.getText().toString();

                // Passwords now match
                if (pwd1.equals(pwd2)) {
                    pwdMsg.setText("");
                    pwdIcon.setImageResource(R.drawable.circle_check);
                    matchPwd = true;
                    last_state = 1;
                }

                // Passwords no longer match
                else if (last_state==1) {
                    pwdMsg.setText(R.string.signup_pwdConf_req);
                    pwdIcon.setImageResource(R.drawable.circle_x);
                    matchPwd = false;
                    last_state=0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            Pattern format1 = Pattern.compile("[0-9]{10}"); // 7801234567
            Pattern format2 = Pattern.compile("[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}"); // 780-123-4567

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currPhone = phone.getText().toString();

                // If format matches one of the specified we can accept it
                if (format1.matcher(currPhone).matches() | format2.matcher(currPhone).matches()) {
                    properPhone = true;
                }
                // If format no longer matches toggle boolean
                else if (properPhone) {
                    properPhone = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            // Pattern of "something"@"something"."something"
            // Taken from http://stackoverflow.com/questions/8204680/java-regex-email on November 6, 2016
            Pattern format = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currEmail = email.getText().toString();

                // If the email format matches a proper email is entered
                if (format.matcher(currEmail).matches()) {
                    properEmail = true;
                }
                // If the email no longer matches the format toggle boolean
                else if (properEmail) {
                    properEmail = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Assign focus change listener to allow for unique username check
        usr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int last_state = 0;
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String username = usr.getText().toString();
                    // Nothing Entered
                    if (username.equals("")|username==null) {
                        // Toggle if necessary
                        if (uniqueUsr) {
                            uniqueUsr = false;
                            usrMsg.setText(R.string.signup_usrLen_error);
                            usrIcon.setImageResource(R.drawable.circle_x);
                            last_state=2;
                        }
                        else if (last_state==0) {
                            usrMsg.setText(R.string.signup_usrLen_error);
                            last_state=2;
                        }
                        else {
                            usrMsg.setText(R.string.signup_usrLen_error);
                            usrIcon.setImageResource(R.drawable.circle_x);
                            last_state=2;
                        }
                    }
                    // Something Entered
                    else {
                        User result = new User();
                        ElasticsearchUserController.CheckUserTask checkUserTask = new ElasticsearchUserController.CheckUserTask();
                        checkUserTask.execute(username);
                        try {
                            result = checkUserTask.get();
                        } catch (Exception e) {
                            Log.i("Error", "Failed to get result from asynctask");
                        }

                        // Username has become unique
                        if (result==null & last_state!=1) {
                            uniqueUsr = true;
                            usrIcon.setImageResource(R.drawable.circle_check);
                            usrMsg.setText(R.string.signup_usr_open);
                            last_state = 1;
                        }
                        // Username is still unique
                        else if (result==null & last_state==1) {
                            // Do nothing
                        }
                        // Username is not unique
                        else {
                            uniqueUsr = false;
                            usrIcon.setImageResource(R.drawable.circle_x);
                            usrMsg.setText(R.string.signup_usr_taken);
                            last_state=0;
                        }
                    }
                }
            }
        });


    }

    /**
     * Method to create new user from given sign up information
     *
     * Uses password encryption method taken from
     * http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
     * on November 6, 2016
     *
     * @return newUser : User, null if unable to create password hash
     */
    private User createNewUser() {
        String userName = usr.getText().toString();
        String userEmail = email.getText().toString();
        String userPhone = phone.getText().toString();
        String passWord;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pwd.getText().toString().getBytes("UTF-8"));
            passWord = md.digest().toString();
        } catch (Exception e) {
            Log.i("Error", "Failed to create password hash");
            e.printStackTrace();
            return null;
        }

        return new User(userName, passWord, userEmail, userPhone);
    }

    /**
     * Method that assigns all variables to respective views
     */
    private void assignViews() {
        errorMsg = (TextView) findViewById(R.id.activity_signup_errorMsg);
        usr = (EditText) findViewById(R.id.activity_signup_usr);
        usrIcon = (ImageView) findViewById(R.id.activity_signup_usrIcon);
        usrMsg = (TextView) findViewById(R.id.activity_signup_usrMsg);
        pwd = (EditText) findViewById(R.id.activity_signup_pwd);
        pwdConf = (EditText) findViewById(R.id.activity_signup_pwdConf);
        pwdIcon = (ImageView) findViewById(R.id.activity_signup_pwdIcon);
        pwdStrength.add((TextView) findViewById(R.id.activity_signup_weakPwd));
        pwdStrength.add((TextView) findViewById(R.id.activity_signup_goodPwd));
        pwdStrength.add((TextView) findViewById(R.id.activity_signup_strongPwd));
        pwdMsg = (TextView) findViewById(R.id.activity_signup_pwdMsg);
        email = (EditText) findViewById(R.id.activity_signup_email);
        phone = (EditText) findViewById(R.id.activity_signup_phone);
        signupButton = (Button) findViewById(R.id.activity_signup_button);
    }

    /**
     * Method that builds & displays error string according to missing criteria
     */
    private void displayErrorStr() {
        String errorStr = "";
        if (!uniqueUsr) {
            errorStr += getResources().getString(R.string.signup_usr_taken) + "\n";
        }
        if (!matchPwd) {
            errorStr += getResources().getString(R.string.signup_pwdMatch_error) + "\n";
        }
        if (!strongPwd) {
            errorStr += getResources().getString(R.string.signup_pwdStr_error) + "\n";
        }
        if (!properEmail) {
            errorStr += getResources().getString(R.string.signup_email_error) + "\n";
        }
        if (!properPhone) {
            errorStr += getResources().getString(R.string.signup_phone_error);
        }
        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.setText(errorStr);
    }
}
