package com.stevensadler.android.bloquery.ui.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;
import com.stevensadler.android.bloquery.ui.fragment.AddQuestionDialogFragment;
import com.stevensadler.android.bloquery.ui.fragment.GenericMessageFragment;
import com.stevensadler.android.bloquery.ui.fragment.IDelegatingFragment;
import com.stevensadler.android.bloquery.ui.fragment.IFragmentDelegate;
import com.stevensadler.android.bloquery.ui.fragment.ProfileEditorFragment;
import com.stevensadler.android.bloquery.ui.fragment.QuestionListFragment;
import com.stevensadler.android.bloquery.ui.fragment.SingleQuestionFragment;

public class BloqueryActivity extends AppCompatActivity implements
        IFragmentDelegate,
        AddQuestionDialogFragment.AddQuestionDialogListener,
        ProfileEditorFragment.Delegate,
        QuestionListFragment.Delegate,
        SingleQuestionFragment.Delegate,
        GenericMessageFragment.Delegate {

    private static String TAG = BloqueryActivity.class.getSimpleName();

    private String AIRPLANE_MODE_ON_MESSAGE = "Airplane mode is on. No questions are available. Please turn Airplane mode off and click this text to view questions.";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloquery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        TestQuestionCreator testQC = new TestQuestionCreator();
//        testQC.addQuestion("Public write access to untracked author question created by " + ParseUser.getCurrentUser().getUsername());
//        testQC.addQuestion("An untracked author question created by " + ParseUser.getCurrentUser().getUsername());
//        testQC.addQuestion("A question created by " + ParseUser.getCurrentUser().getUsername());
//        testQC.addQuestion("Test question from" + ParseUser.getCurrentUser().getUsername());

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

//                TestQuestionCreator testQC = new TestQuestionCreator();
//                testQC.addQuestion("How do you add an empty array of answers to a question?");

                BloqueryApplication.getSharedNetworkManager().pullCurrentUserProfile();
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
            addGenericMessageFragment(AIRPLANE_MODE_ON_MESSAGE);

        } else if (savedInstanceState == null) {
            // the activity IS NOT being re-created
            addQuestionListFragment();
        } else {
            // the activity IS being re-created
            int count = getFragmentManager().getBackStackEntryCount();
            for (int index = 0; index < count ; index++) {
                String name = getFragmentManager().getBackStackEntryAt(index).getName();
                Log.d(TAG, "re-create " + index +  " " + name);
                try {
                    IDelegatingFragment iDelegatingFragment = (IDelegatingFragment) getFragmentManager().findFragmentByTag(name);
                    if (iDelegatingFragment != null) {
                        iDelegatingFragment.setDelegate(this);
                    }

                } catch (ClassCastException e) {
                    // ignore non-delegating fragments
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onQuestionListClicked(ParseObject question) {
        Log.d(TAG, "onQuestionListClicked " + question.getString("body"));
        addSingleQuestionFragment(question);
    }

    /*
     * QuestionListFragment.Delegate
     * SingleQuestionFragment.Delegate
     */
    @Override
    public void onUserProfileImageClicked(ParseUser parseUser) {
        if (ParseUser.getCurrentUser().getObjectId().equals(parseUser.getObjectId())) {
            // do something with current user profile image click, like open profile editor instead of profile view
            Log.d(TAG, "currentUser profile click - need to open profile viewer or editor");
        } else {
            // open profile view
            Log.d(TAG, "some other user profile click - need to open profile viewer");
        }
    }

    /*
     * SingleQuestionFragment.Delegate
     */
    public void onSingleQuestionClicked(ParseObject question) {
        Log.d(TAG, "onSingleQuestionClicked " + question.getString("body"));
        getFragmentManager().popBackStack();
    }

    public void onSubmitAnswerClicked(ParseObject question, String answerBody) {
        Log.d(TAG, "onSubmitAnswerClicked " + answerBody);

        BloqueryApplication.getSharedNetworkManager().addAnswer(question, answerBody);
        getFragmentManager().popBackStack();
    }

    /*
     * GenericMessageFragment.Delegate
     */
    public void onGenericMessageClicked(String genericMessage) {
        Log.d(TAG, "onGenericMessageClicked " + genericMessage);
        if (!isAirplaneModeOn(this)) {
            addQuestionListFragment();
        }
    }

    /*
     * ProfileEditorFragment.Delegate
     */
    public void onProfileSaveClicked(Bitmap bitmap, String description) {
        Log.d(TAG, "onProfileSaveClicked " + description);

        //ParseObject profile = BloqueryApplication.getSharedDataSource().getCurrentUserProfile();
        //BloqueryApplication.getSharedNetworkManager().saveProfile(profile, bitmap, description);
        BloqueryApplication.getSharedNetworkManager().saveProfile(bitmap, description);
        getFragmentManager().popBackStack();
    }

    /*
     * AddQuestionDialogFragment.AddQuestionDialogListener
     */
    @Override
    public void onFinishAddQuestionDialog(String questionBody) {
        BloqueryApplication.getSharedNetworkManager().addQuestion(questionBody);
    }

    /*
     * public android:onClick functions in menu_bloquery.xml
     */
    public void onMenuLogoutClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuLogoutClick");

        ParseUser.logOut();

        // send user to LoginSignupActivity.class
        Intent intent = new Intent(BloqueryActivity.this,
                LoginSignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void onMenuProfileClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuProfileClick 2");
        addProfileEditorFragment();
    }

    public void onMenuRefreshDataClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuRefreshDataClick");
        BloqueryApplication.getSharedNetworkManager().pullQuestions();
    }

    public void onMenuAddQuestionClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuAddQuestionClick");

        AddQuestionDialogFragment dialogFragment = new AddQuestionDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "Add Question Dialog Fragment");
    }

    /*
     * Private methods
     */

    private void addFragment(Fragment fragment, String tag) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private void addQuestionListFragment() {
        if (isAirplaneModeOn(this)) {
            //Toast.makeText(this, "Airplane mode is on. No questions are available.", Toast.LENGTH_LONG).show();
            addGenericMessageFragment(AIRPLANE_MODE_ON_MESSAGE);
            return;
        }

        QuestionListFragment fragment = new QuestionListFragment();
        fragment.setDelegate(this);
        BloqueryApplication.getSharedDataSource().addObserver(fragment);
        addFragment(fragment, "TagQuestionListFragment");
    }

    private void addSingleQuestionFragment(final ParseObject question) {
        if (isAirplaneModeOn(this)) {
            //Toast.makeText(this, "Airplane mode is on. No questions are available.", Toast.LENGTH_LONG).show();
            addGenericMessageFragment(AIRPLANE_MODE_ON_MESSAGE);
            return;
        }

        Log.d(TAG, "addSingleQuestionFragment " + question.getObjectId() + " " + question.getString("body"));

        SingleQuestionFragment fragment = new SingleQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("questionObjectId", question.getObjectId());
        fragment.setArguments(bundle);
        fragment.setDelegate(this);
        BloqueryApplication.getSharedDataSource().addObserver(fragment);
        addFragment(fragment, "TagSingleQuestionFragment");
    }

    private void addGenericMessageFragment(String message) {

        Log.d(TAG, "addGenericMessageFragment " + message);

        GenericMessageFragment fragment = new GenericMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        fragment.setDelegate(this);
        addFragment(fragment, "TagGenericMessageFragment");
    }

    private void addProfileEditorFragment() {

        Log.d(TAG, "addProfileEditorFragment");

        ProfileEditorFragment fragment = new ProfileEditorFragment();
        fragment.setDelegate(this);
        addFragment(fragment, "TagProfileEditorFragment");
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
