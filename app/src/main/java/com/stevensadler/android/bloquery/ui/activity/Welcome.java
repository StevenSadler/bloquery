package com.stevensadler.android.bloquery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;

/**
 * Created by Steven on 12/31/2015.
 */
public class Welcome extends AppCompatActivity {

    Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String stringUser = currentUser.getUsername().toString();
        TextView textUser = (TextView) findViewById(R.id.tv_user);
        textUser.setText("You are logged in as " + stringUser);

        logout = (Button) findViewById(R.id.b_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout current user
                ParseUser.logOut();

                // send user to LoginSignupActivity.class
                Intent intent = new Intent(Welcome.this,
                        LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
