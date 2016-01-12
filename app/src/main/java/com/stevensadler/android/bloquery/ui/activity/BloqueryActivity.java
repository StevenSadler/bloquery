package com.stevensadler.android.bloquery.ui.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Answer;
import com.stevensadler.android.bloquery.api.model.ParseProxyObject;
import com.stevensadler.android.bloquery.api.model.Question;
import com.stevensadler.android.bloquery.ui.fragment.GenericMessageFragment;
import com.stevensadler.android.bloquery.ui.fragment.QuestionListFragment;
import com.stevensadler.android.bloquery.ui.fragment.SingleQuestionFragment;

import java.util.List;

public class BloqueryActivity extends AppCompatActivity implements QuestionListFragment.Delegate, SingleQuestionFragment.Delegate, GenericMessageFragment.Delegate {

    private static String TAG = BloqueryActivity.class.getSimpleName();

    private String AIRPLANE_MODE_ON_MESSAGE = "Airplane mode is on. No questions are available. Please turn Airplane mode off and click this text to view questions.";

    private boolean hasAddedFirstFragment = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloquery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        TestQuestionCreator testQC = new TestQuestionCreator();
//        testQC.addQuestion("What would you do if you were the one survivor in a plane crash");
//        testQC.addQuestion("If you woke up and had 2,000 unread emails and could only answer 300 of them how would you choose which ones to answer?");
//        testQC.addQuestion("Who would win a fight between Spiderman and Batman");
//        testQC.addQuestion("What did you have for breakfast?");
//        testQC.addQuestion("What is your favorite 90's jam?");
//        testQC.addQuestion("If you had a machine that produced $100 for life, what would you be willing to pay for it?");
//        testQC.addQuestion("Describe the color yellow to someone who is blind");
//        testQC.addQuestion("If you were asked to unload a 747 full of jellybeans, what would you do?");
//        testQC.addQuestion("How many people flew out of Chicago last year?");
//        testQC.addQuestion("Who is your favorite Disney princess?");
//        testQC.addQuestion("Why is the third hand on a watch called the second hand?");
//        testQC.addQuestion("Why isn't there mouse-flavored cat food?");
//        testQC.addQuestion("Why is the time of day with the slowest traffic called rush hour?");
//        testQC.addQuestion("Why is abbreviated such a long word?");
//        testQC.addQuestion("Why is a boxing ring square?");
//        testQC.addQuestion("What do you call a male ladybug?");
//        testQC.addQuestion("If a person owns a piece of land, do they own it all the down to the core of the Earth?");
//        testQC.addQuestion("Why isn't phonetic spelled the way it sounds?");
//        testQC.addQuestion("Why are there interstates in Hawaii?");
//        testQC.addQuestion("Why are there flotation devices in the seats of airplanes instead of parachutes?");
//        testQC.addQuestion("Have you ever imagined a world without hypothetical situations?");
//        testQC.addQuestion("Why don't sheep shrink when it rains?");
//        testQC.addQuestion("Why do toasters always have a setting that burns the toast to a horrible crisp no one would eat?");

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

        if (isAirplaneModeOn(this)) {
//            Question question = new Question();
//            question.setBody("Airplane mode is on. No questions are available. Please turn Airplane mode off and click this text to view questions.");
//            addSingleQuestionFragment(question);

            addGenericMessageFragment(AIRPLANE_MODE_ON_MESSAGE);

        } else {
            addQuestionListFragment();
        }
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
        Log.d(TAG, "onQuestionListClicked " + question.getBody());
        addSingleQuestionFragment(question);
    }

    /*
     * SingleQuestionFragment.Delegate
     */
    public void onQuestionClicked(SingleQuestionFragment singleQuestionFragment, Question question) {
        // TODO: go to question list view
        Log.d(TAG, "onSingleQuestionClicked " + question.getBody());
        addQuestionListFragment();
    }

    public void onSubmitAnswerClicked(SingleQuestionFragment singleQuestionFragment, Answer answer) {
        // TODO: go to question list view
        Log.d(TAG, "onSubmitAnswerClicked " + answer.getBody());
        addQuestionListFragment();
    }

    /*
     * GenericMessageFragment.Delegate
     */
    public void onGenericMessageClicked(GenericMessageFragment genericMessageFragment, String genericMessage) {
        // TODO: go to question list view
        Log.d(TAG, "onGenericMessageClicked " + genericMessage);
        if (!isAirplaneModeOn(this)) {
            addQuestionListFragment();
        }
    }

    /*
     * Private methods
     */

    private void addFragment(Fragment fragment) {
        if (hasAddedFirstFragment) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_container, fragment, null)
                    .addToBackStack(null)
                    .commit();
        } else {
            hasAddedFirstFragment = true;
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_fragment_container, fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void addQuestionListFragment() {
        if (isAirplaneModeOn(this)) {
            //Toast.makeText(this, "Airplane mode is on. No questions are available.", Toast.LENGTH_LONG).show();
            addGenericMessageFragment(AIRPLANE_MODE_ON_MESSAGE);
            return;
        }
        ParseQuery<Question> query = ParseQuery.getQuery("Question");
        query.orderByAscending("body");
        query.setLimit(20);
        query.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "addQuestionListFragment callback: Retrieved " + questions.size() + " questions");

                    QuestionListFragment fragment = new QuestionListFragment();
                    Bundle bundle = new Bundle();

                    int count = 0;
                    for (Question question : questions) {
                        //Log.d(TAG, "onCreate  question[" + count + "] = " + question.getObjectId() + " = " + question.getBody());
                        question.setQuestionId(question.getObjectId());

                        ParseProxyObject parseProxyObject = new ParseProxyObject(question);
                        //Log.d(TAG, "onCreate   ParseProxyObject has questionId = " + parseProxyObject.has("questionId"));
                        bundle.putSerializable("" + count, parseProxyObject);
                        count++;
                    }
                    bundle.putInt("size", count);
                    fragment.setArguments(bundle);
                    fragment.setDelegate(BloqueryActivity.this);
                    addFragment(fragment);
                } else {
                    Log.d(TAG, "addQuestionListFragment callback: Error: " + e.getMessage());
                }
            }
        });
    }

    private void addSingleQuestionFragment(final Question question) {

        if (isAirplaneModeOn(this)) {
            //Toast.makeText(this, "Airplane mode is on. No questions are available.", Toast.LENGTH_LONG).show();
            addGenericMessageFragment(AIRPLANE_MODE_ON_MESSAGE);
            return;
        }

        Log.d(TAG, "addSingleQuestionFragment " + question.getQuestionId() + " = " + question.getBody());

        /*
         * Need to show the questions, an input text field for the answer, and a submit button
         * The questionId will be added to the answer so that it can link to the question table
         */

//        TestAnswerCreator testAC = new TestAnswerCreator();
//        testAC.addAnswer(question, "Test Answer 1");

        ParseQuery<Answer> query = ParseQuery.getQuery("Answer");
        query.whereEqualTo("questionId", question.getQuestionId());
        query.orderByDescending("updatedAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<Answer>() {
            @Override
            public void done(List<Answer> answers, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "addSingleQuestionFragment callback: Retrieved " + answers.size() + " answers");

                    SingleQuestionFragment fragment = new SingleQuestionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("questionId", question.getQuestionId());
                    ParseProxyObject parseProxyQuestion = new ParseProxyObject(question);
                    bundle.putSerializable("questionObject", parseProxyQuestion);

                    int count = 0;
                    for (Answer answer : answers) {
                        Log.d(TAG, "addSingleQuestionFragment callback: answers[" + count + "]  questionId = " + answer.getQuestionId() + " answer.objectId = " + answer.getObjectId() + " = " + answer.getBody());
                        ParseProxyObject parseProxyObject = new ParseProxyObject(answer);
                        bundle.putSerializable(""+count, parseProxyObject);
                        count++;
                    }
                    bundle.putInt("size", count);
                    fragment.setArguments(bundle);
                    fragment.setDelegate(BloqueryActivity.this);
                    addFragment(fragment);
                } else {
                    Log.d(TAG, "addSingleQuestionFragment callback: Error: " + e.getMessage());
                }
            }
        });

    }

    private void addGenericMessageFragment(String message) {

        Log.d(TAG, "addGenericMessageFragment " + message);

        GenericMessageFragment fragment = new GenericMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        fragment.setDelegate(BloqueryActivity.this);
        addFragment(fragment);
    }

    @SuppressWarnings("deprecation")
    private boolean isAirplaneModeOn(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1){
        /* API 17 and above */
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        } else {
        /* below */
            return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
    }
}
