package com.stevensadler.android.bloquery.api.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.network.NetworkManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Created by Steven on 1/12/2016.
 */
public class DataSource extends Observable implements
        NetworkManager.Delegate {

    private String TAG = DataSource.class.getSimpleName();

    private ParseObject mSingleQuestion;
    private List<ParseObject> mQuestions;
    private Bitmap mCurrentUserProfileImage;
    private String mCurrentUserProfileDescription;
    private HashMap<String, Bitmap> mUserProfileImages;

    private int pullImagesCount;

    public DataSource() {
        mQuestions = new ArrayList<ParseObject>();
        mUserProfileImages = new HashMap<String, Bitmap>();
        pullImagesCount = 0;
    }

    public List<ParseObject> getQuestions() {
        return mQuestions;
    }
    public String getCurrentUserProfileDescription() {
        return mCurrentUserProfileDescription;
    }
    public Bitmap getCurrentUserProfileImage() {
        return mCurrentUserProfileImage;
    }

    public Bitmap getUserProfileImage(String userId) {
        Log.d(TAG, "getUserProfileImage for user " + userId);
        Bitmap bitmap = null;
        if (mUserProfileImages.containsKey(userId)) {
            bitmap = mUserProfileImages.get(userId);
        }
        return bitmap;
    }

    public ParseObject getQuestionById(String questionObjectId) {
        // if questions have been added such that the current question viewed is no longer in the pull set
        // then get the question from mSingleQuestion
        if (mSingleQuestion != null && mSingleQuestion.getObjectId().equals(questionObjectId)) {
            return mSingleQuestion;
        }
        for (ParseObject question : mQuestions) {
            if (question.getObjectId().equals(questionObjectId)) {
                return question;
            }
        }
        return null;
    }

    /*
     * NetworkManager.Delegate
     */
    @Override
    public void onPullQuestions(List<ParseObject> questions) {
        Log.d(TAG, "Retrieved " + questions.size() + " questions");
        for (ParseObject question :questions) {
            Log.d(TAG, question.getObjectId() + " " + question.getString("body"));
            ParseUser questionAuthor = question.getParseUser("createdBy");

            // only use this while testing
            if (questionAuthor == null) {
                Log.d(TAG, "questionAuthor IS null");
            } else {
                if (mUserProfileImages.containsKey(questionAuthor.getObjectId())) {
                    Log.d(TAG, "questionAuthor IS NOT null, and IS in mUserProfileImages");
                } else {
                    Log.d(TAG, "questionAuthor IS NOT null, and IS NOT in mUserProfileImages");
                }
            }

            if (questionAuthor != null && !mUserProfileImages.containsKey(questionAuthor.getObjectId())) {

                mUserProfileImages.put(questionAuthor.getObjectId(), null);
                addUserProfileImage(questionAuthor);

                List<ParseObject> answers = question.getList("answerList");
                for (ParseObject answer : answers) {
                    ParseUser answerAuthor = answer.getParseUser("createdBy");
                    if (answerAuthor != null && !mUserProfileImages.containsKey(answerAuthor.getObjectId())) {

                        mUserProfileImages.put(answerAuthor.getObjectId(), null);
                        addUserProfileImage(answerAuthor);
                    }
                }
            }
        }
        mQuestions = questions;

        setChanged();
        notifyObservers();
        clearChanged();
    }

    @Override
    public void onPullSingleQuestion(ParseObject currentQuestion) {
        for (ParseObject question :mQuestions) {
            if (question.getObjectId().equals(currentQuestion.getObjectId())) {
                question.put("answerList", currentQuestion.get("answerList"));
                setChanged();
                notifyObservers();
                clearChanged();
                return;
            }
        }

        mSingleQuestion = currentQuestion;
        setChanged();
        notifyObservers();
        clearChanged();
    }

    @Override
    public void onCurrentUserProfileUpdate() {
        // clear old data references
        mCurrentUserProfileDescription = "";
        mCurrentUserProfileImage = null;

        // set new data references
        ParseUser currentUser = ParseUser.getCurrentUser();
        mCurrentUserProfileDescription = currentUser.getString("profileDescription");
        ParseFile parseFile = currentUser.getParseFile("profileImage");
        mCurrentUserProfileImage = ImageFile.parseFileToBitmap(parseFile);



//        setChanged();
//        notifyObservers();
//        clearChanged();
    }

    /*
     *
     */
    public Boolean didUserVoteOnAnswer(ParseUser parseUser, ParseObject currentAnswer) {
        List<ParseObject> answers = parseUser.getList("upvotedAnswerList");
        for (ParseObject answer : answers) {
            if (answer.getObjectId().equals(currentAnswer.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    /*
     * private helper functions
     */
    private void addUserProfileImage(final ParseUser user) {

        ParseFile parseFile = null;
        try {
            //parseFile = user.getParseFile("profileImage");
            parseFile = user.fetchIfNeeded().getParseFile("profileImage");
        } catch (IllegalStateException e) {
            // ignore users that have no profileImage
            Log.d(TAG, "ignore users that have no profileImage");
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            Log.d(TAG, "ignore users that have no profileImage");
            e.printStackTrace();
        }

        if (parseFile != null) {
            Log.d(TAG, "parseFile IS NOT null, so should be added to mUserProfileImages");
            pullImagesCount++;
            parseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "addUserProfileImage  user " + user.getUsername());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        Log.d(TAG, "mUserProfileImages.size = " + mUserProfileImages.size());
                        mUserProfileImages.put(user.getObjectId(), bitmap);
                        Log.d(TAG, "mUserProfileImages.size = " + mUserProfileImages.size());
                    } else {
                        e.printStackTrace();
                    }
                    pullImagesCount--;
                    if (pullImagesCount == 0) {
                        setChanged();
                        notifyObservers();
                        clearChanged();
                    }
                }
            });
        } else {
            Log.d(TAG, "parseFile IS null, because user has not saved a profile image?");
            mUserProfileImages.remove(user.getObjectId());
        }
    }
}
