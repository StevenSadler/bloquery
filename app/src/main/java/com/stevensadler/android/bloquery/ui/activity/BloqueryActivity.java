package com.stevensadler.android.bloquery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Question;
import com.stevensadler.android.bloquery.ui.fragment.QuestionListFragment;

import java.util.List;

public class BloqueryActivity extends AppCompatActivity {

    private static String TAG = BloqueryActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloquery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("fooo", "barr");
//        testObject.saveInBackground();

//        TestQuestionCreator testQC = new TestQuestionCreator();
//        testQC.addQuestion("What would you do if you were the one survivor in a plane crash");
//        testQC.addQuestion("If you woke up and had 2,000 unread emails and could only answer 300 of them how would you choose which ones to answer?");
//        testQC.addQuestion("Who would win a fight between Spiderman and Batman");
//        testQC.addQuestion("What did you have for breakfast?");

        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // if user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(BloqueryActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
            finish();
        } else {
            // if current user is NOT anonymous user
            // get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // send logged in users to Welcome.class
//                Intent intent = new Intent(BloqueryActivity.this, Welcome.class);
//                startActivity(intent);
//                finish();

                Toast.makeText(this, "Welcome " + currentUser.getUsername(), Toast.LENGTH_LONG).show();
            } else {
                // send user to LoginSignupActivity.class
                Intent intent = new Intent(BloqueryActivity.this,
                        LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

        ParseQuery<Question> query = ParseQuery.getQuery("Question");
        query.orderByAscending("body");
        query.setLimit(3);
        query.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null) {
                    Log.d("questions", "Retrieved " + questions.size() + " questions");

                    getFragmentManager()
                            .beginTransaction()
                            .add(R.id.rv_fragment_question_list, QuestionListFragment.fragmentForQuestions(questions))
                            .commit();
                } else {
                    Log.d("questions", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bloquery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * QuestionListFragment.Delegate
     */
    public void onQuestionClicked(QuestionListFragment questionListFragment, Question question) {
        // TODO: go to single question view
        Log.d(TAG, "onQuestionClicked " + question.getBody());
    }
}
