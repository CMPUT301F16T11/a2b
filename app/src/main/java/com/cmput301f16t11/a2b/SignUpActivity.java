package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.regex.Pattern;

/**
 *  Activity that prompts user to fill out required information.
 *
 *  Checks for unique username & a strong password before creating user.
 */
public class SignUpActivity extends AppCompatActivity {

    // Views from activity_sign_up.xml
    private TextView errorMsg;
    private RadioButton rider;
    private RadioButton both;
    private EditText usr;
    private ImageView usrIcon;
    private TextView usrMsg;
    private EditText email;
    private EditText phone;
    private Button signupButton;
    private RelativeLayout carLayout;
    private EditText carYear;
    private EditText carMake;
    private EditText carModel;
    private EditText carColor;


    // Sign up data test variables
    protected Boolean uniqueUsr = false;
    protected Boolean properEmail = false;
    protected Boolean properPhone = false;
    protected Boolean properYear = false;
    protected Boolean properMake = false;
    protected Boolean properModel = false;
    protected Boolean properColor = false;
    protected Boolean type = false; // False for both, true for driver
    protected Boolean error = false;

    protected int last_state = 0;

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
                // Run check user task if user is still selected
                if (usr.hasFocus()) {
                    checkUser();
                }


                // Case where all criteria are met
                if (uniqueUsr & properEmail & properPhone & !type
                        & properYear & properMake & properModel & properColor) {
                    // Remove errorMsg if necessary
                    if (error) {
                        errorMsg.setText("");
                        errorMsg.setVisibility(View.GONE);
                        error = false;
                    }

                    ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
                    User newUser = createNewUser(type);

                    // Attempt to add user
                    addUserTask.execute(newUser);
                    try {
                        Boolean result = addUserTask.get();

                        if (result) {
                            UserController.setUser(newUser);
                            Intent intent =
                                    new Intent(SignUpActivity.this, RiderLocationActivity.class);
                            startActivity(intent);
                            finish();
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

                } else if (uniqueUsr & properEmail & properPhone & type) {
                    // Remove errorMsg if necessary
                    if (error) {
                        errorMsg.setText("");
                        errorMsg.setVisibility(View.GONE);
                        error = false;
                    }

                    ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
                    User newUser = createNewUser(type);

                    // Attempt to add user
                    addUserTask.execute(newUser);
                    try {
                        Boolean result = addUserTask.get();

                        if (result) {
                            UserController.setUser(newUser);
                            Intent intent =
                                    new Intent(SignUpActivity.this, RiderLocationActivity.class);
                            startActivity(intent);
                            finish();
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
                // Not all criteria met -- display required error messages
                else {
                    error = true;
                    displayErrorStr();
                }

            }
        });

        rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((RadioButton)v).isChecked() & !type) {
                    carLayout.setVisibility(View.GONE);
                    type = true;
                }
            }
        });

        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((RadioButton)v).isChecked() & type)  {
                    carLayout.setVisibility(View.VISIBLE);
                    type = false;
                }
            }
        });

        // Assign TextWatcher to determine if criteria is met
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

        carYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SignUpActivity.this.carYear.getText().toString();
                if (text.length()>0 & !properYear) {
                    properYear = true;
                } else if(text.length()==0 & properYear) {
                    properYear = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        carMake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SignUpActivity.this.carMake.getText().toString();
                if (text.length()>0 & !properMake) {
                    properMake = true;
                } else if (text.length()==0 & properMake) {
                    properMake = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        carModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SignUpActivity.this.carModel.getText().toString();
                if (text.length()>0 & !properModel) {
                    properModel = true;
                } else if (text.length()==0 & properModel) {
                    properModel = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        carColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = SignUpActivity.this.carColor.getText().toString();
                if (text.length()>0 & !properColor) {
                    properColor = true;
                } else if (text.length()==0 & properColor) {
                    properColor = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Assign focus change listener to allow for unique username check
        usr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUser();
                }
            }
        });


    }

    /**
     * Check if the username entered is valid and unique
     */
    public void checkUser() {
        String username = usr.getText().toString();
        // Nothing Entered
        if (username.equals("")||username==null) {
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
            User nullUser = new User();
            ElasticsearchUserController.CheckUserTask checkUserTask = new ElasticsearchUserController.CheckUserTask();
            checkUserTask.execute(username);
            try {
                result = checkUserTask.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get result from asynctask");
            }

            // Username has become unique
            if (result.getName()==null & last_state!=1) {
                uniqueUsr = true;
                usrIcon.setImageResource(R.drawable.circle_check);
                usrMsg.setText(R.string.signup_usr_open);
                last_state = 1;
            }
            // Username is still unique
            else if (result.getName()==null & last_state==1) {
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

    /**
     * Method to create new user from given sign up information
     *
     *
     * @return newUser : User
     */
    private User createNewUser(Boolean type) {
        String userName = usr.getText().toString();
        String userEmail = email.getText().toString();
        String userPhone = phone.getText().toString();
        Vehicle car;

        if (!type) {
            int year = Integer.valueOf(carYear.getText().toString());
            String make = carMake.getText().toString();
            String model = carModel.getText().toString();
            String color = carColor.getText().toString();
            car = new Vehicle(make, model, color, year);
            return new User(userName, userEmail, userPhone, car);
        } else {
            return new User(userName, userEmail, userPhone);
        }
    }

    /**
     * Method that assigns all variables to respective views
     */
    private void assignViews() {
        errorMsg = (TextView) findViewById(R.id.activity_signup_errorMsg);
        rider = (RadioButton) findViewById(R.id.activity_signup_riderButton);
        both = (RadioButton) findViewById(R.id.activity_signup_bothButton);
        usr = (EditText) findViewById(R.id.activity_signup_usr);
        usrIcon = (ImageView) findViewById(R.id.activity_signup_usrIcon);
        usrMsg = (TextView) findViewById(R.id.activity_signup_usrMsg);
        email = (EditText) findViewById(R.id.activity_signup_email);
        phone = (EditText) findViewById(R.id.activity_signup_phone);
        carLayout = (RelativeLayout) findViewById(R.id.activity_signup_carLayout);
        carYear = (EditText) findViewById(R.id.activity_signup_carYear);
        carMake = (EditText) findViewById(R.id.activity_signup_carMake);
        carModel = (EditText) findViewById(R.id.activity_signup_carModel);
        carColor = (EditText) findViewById(R.id.activity_signup_carColor);
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
        if (!properEmail) {
            errorStr += getResources().getString(R.string.signup_email_error) + "\n";
        }
        if (!properPhone) {
            errorStr += getResources().getString(R.string.signup_phone_error);
        }
        if (!properYear & !type) {
            errorStr += getResources().getString(R.string.signup_carYear_error) + "\n";
        }
        if (!properMake & !type) {
            errorStr += getResources().getString(R.string.signup_carMake_error) + "\n";
        }
        if (!properModel & !type) {
            errorStr += getResources().getString(R.string.signup_carModel_error) + "\n";
        }
        if (!properColor & !type) {
            errorStr += getResources().getString(R.string.signup_carColor_error) + "\n";
        }
        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.setText(errorStr);
    }
}
