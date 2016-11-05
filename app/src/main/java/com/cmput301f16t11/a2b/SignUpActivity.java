package com.cmput301f16t11.a2b;

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

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *  Activity that prompts user to fill out required information.
 *
 *  Checks for unique username & a strong password before creating user.
 */
public class SignUpActivity extends AppCompatActivity {

    // Views from activity_sign_up.xml
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
    private Boolean uniqueUsr = false;
    private Boolean matchPwd = false;
    private Boolean strongPwd = false;
    private Boolean properEmail = false;
    private Boolean properPhone = false;

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
                //TODO: Add user if requirements are met, otherwise display what is still needed
                if (uniqueUsr & matchPwd & strongPwd & properEmail & properPhone) {

                }
            }
        });

        // Assign TextWatcher to determine password strength & matching passwords
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

        // Assign focus change listener to allow for unique username check & confirm proper email/phone
        usr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            int last_state;
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //Start the AsyncTask to see if the entered username is already taken
                    String username = usr.getText().toString();
                    Boolean result = false;
                    ElasticsearchUserController.CheckUserTask checkUserTask = new ElasticsearchUserController.CheckUserTask();
                    checkUserTask.execute(username);
                    try {
                        result = checkUserTask.get();
                    } catch (Exception e) {
                        Log.i("Error", "Failed to get result from asynctask");
                    }

                    // Handle results accordingly
                    if (!result & last_state==0) {
                        uniqueUsr = true;
                        usrIcon.setImageResource(R.drawable.circle_check);
                        usrMsg.setText(R.string.signup_usr_open);
                        last_state = 1;
                    } else {
                        uniqueUsr = false;
                        usrIcon.setImageResource(R.drawable.circle_x);
                        usrMsg.setText(R.string.signup_usr_taken);
                        last_state=0;
                    }
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Pattern of "something"@"something"."something"
            Pattern format = Pattern.compile(".*@.*\\..*");
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String currEmail = email.getText().toString();

                // Once it is out of focus we verify if it's a proper email
                if (!hasFocus) {
                    // If the email format matches a proper email is entered
                    if (format.matcher(currEmail).matches()) {
                        properEmail = true;
                    }
                    // If the email no longer matches the format
                    else if (properEmail==true) {
                        properEmail = false;
                    }
                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            Pattern format1 = Pattern.compile("[0-9]{10}"); // 7801234567
            Pattern format2 = Pattern.compile("[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}"); // 780-123-4567
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String currPhone = phone.getText().toString();

                if (!hasFocus) {
                    // If format matches one of the specified we can accept it
                    if (format1.matcher(currPhone).matches() || format2.matcher(currPhone).matches()) {
                        properPhone = true;
                    }
                    // If format no longer matches toggle boolean
                    else if (properPhone == true) {
                        properPhone = false;
                    }
                }
            }
        });


    }

    private void assignViews() {
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
}
