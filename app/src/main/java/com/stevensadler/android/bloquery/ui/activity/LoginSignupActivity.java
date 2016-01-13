package com.stevensadler.android.bloquery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.stevensadler.android.bloquery.R;

/**
 * Created by Steven on 12/31/2015.
 */
public class LoginSignupActivity extends AppCompatActivity {

    Button loginButton;
    Button signupButton;
    String usernameText;
    String passwordText;
    EditText password;
    EditText username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_signup);

        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        loginButton = (Button) findViewById(R.id.b_login);
        signupButton = (Button) findViewById(R.id.b_signup);

        // loginButton click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText = username.getText().toString();
                passwordText = password.getText().toString();

                ParseUser.logInInBackground(usernameText, passwordText,
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // if user exists and is authenticated, send user to Welcome.class
                                    Intent intent = new Intent(
                                            LoginSignupActivity.this,
                                            Welcome.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Logged in",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "No such user exists, please signup",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // signupButton click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText = username.getText().toString();
                passwordText = password.getText().toString();

                // force user to fill out the form
                if (usernameText.equals("") && passwordText.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the signup form",
                            Toast.LENGTH_LONG).show();
                } else {
                    // save new user data into Parse.com data storage
                    ParseUser user = new ParseUser();
                    user.setUsername(usernameText);
                    user.setPassword(passwordText);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Successfully signed up, please log in.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
