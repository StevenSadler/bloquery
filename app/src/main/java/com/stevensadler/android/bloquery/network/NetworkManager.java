package com.stevensadler.android.bloquery.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stevensadler.android.bloquery.api.model.ImageFile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 1/14/2016.
 */
public class NetworkManager {

    public interface Delegate {
        public void onPullQuestions(List<ParseObject> objects);
        public void onCurrentUserProfileUpdate();
        public void onPullSingleQuestion(ParseObject question);
        //public void onPullCurrentUserProfile(byte[] data, String description);
    }

    private String TAG = NetworkManager.class.getSimpleName();

    private WeakReference<Delegate> delegate;

    public NetworkManager() {
    }

    public void pullQuestions() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.include("answerList");
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (getDelegate() != null) {
                        getDelegate().onPullQuestions(objects);
                    }
                } else {
                    Log.d(TAG, "parse query: Error: " + e.getMessage());
                }
            }
        });
    }

    public void pullSingleQuestion(String questionId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.include("answerList");
        query.getInBackground(questionId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (getDelegate() != null) {
                        getDelegate().onPullSingleQuestion(object);
                    }
                } else {
                    Log.d(TAG, "parse query: Error: " + e.getMessage());
                }
            }
        });
    }

    public void pullCurrentUserProfile() {

        if (getDelegate() != null) {
            getDelegate().onCurrentUserProfileUpdate();
        }

//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser != null) {
//            final String profileDescription = currentUser.getString("profileDescription");
//            ParseFile parseFile = currentUser.getParseFile("profileImage");
//
//            parseFile.getDataInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    if (e != null) {
//                        if (getDelegate() != null) {
//                            getDelegate().onPullCurrentUserProfile(data, profileDescription);
//                        }
//                    } else {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }


//    public void createUserProfile() {
//        // create a new empty profile with only placeholder description
//        final ParseObject profile = new ParseObject("Profile");
//        profile.put("description","Description of " + ParseUser.getCurrentUser().getUsername());
//        profile.put("createdBy", ParseUser.getCurrentUser());
//        profile.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d(TAG, "New Profile created");
//                    if (getDelegate() != null) {
//                        getDelegate().onPullCurrentUserProfile(profile);
//                    }
//                } else {
//                    Log.d(TAG, "Profile creation Error");
//                }
//            }
//        });
//    }

    public void addQuestion(String body) {
        if (body == null || body == "") {
            return;
        }
        ParseObject question = ParseObject.create("Question");
        question.put("body", body);
        question.put("createdBy", ParseUser.getCurrentUser());
        question.put("answerList", new ArrayList<ParseObject>());
        question.saveInBackground();

        // when i want to notify a delegate that I have completed a data change on parse
        // and the delegate or something else might want to refresh by pulling data again
        // then i need to tell that delegate it needs to do something

//        question.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    if (getDelegate() != null) {
//                        getDelegate().onAddQuestion();
//                    }
//                } else {
//                    Log.d(TAG, "parse query: Error: " + e.getMessage());
//                }
//            }
//        });
    }

    public void addAnswer(ParseObject question, String body) {
        if (question == null || body == null || body == "") {
            return;
        }
        ParseObject answer = ParseObject.create("Answer");
        answer.put("body", body);
        answer.put("createdBy", ParseUser.getCurrentUser());
        answer.put("upvoteCount", 0);
        answer.saveInBackground();

        question.add("answerList", answer);
        question.saveInBackground();
    }

    public void saveProfile(Bitmap bitmap, String description) {

        ImageFile imageFile = new ImageFile("user.jpeg", bitmap);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("profileImage", imageFile);
        currentUser.put("profileDescription", description);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Profile edited");
                    if (getDelegate() != null) {
                        getDelegate().onCurrentUserProfileUpdate();
                    }
                } else {
                    Log.d(TAG, "Profile edit Error");
                }
            }
        });

    }

    public void upvoteAnswer(ParseObject currentAnswer, final String questionId) {
        Log.d(TAG, "upvoteAnswer");
        ParseUser currentUser = ParseUser.getCurrentUser();
        List<ParseObject> answers = currentUser.getList("upvotedAnswerList");
        for (ParseObject answer : answers) {
            if (answer.getObjectId().equals(currentAnswer.getObjectId())) {


                return;
            }
        }

        // add the answer to the user's upvotedAnswerList
        // increment the upvote count on the answer
        currentUser.add("upvotedAnswerList", currentAnswer);
        currentUser.saveInBackground();

        currentAnswer.increment("upvoteCount");
        //currentAnswer.saveInBackground();

        currentAnswer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Answer upvote increment, for now pull questions");
                    pullSingleQuestion(questionId);
//                    if (getDelegate() != null) {
//                        getDelegate().onAnswerVoteUpdate();
//                    }
                } else {
                    Log.d(TAG, "Answer upvote increment Error");
                }
            }
        });
    }

    public void downvoteAnswer(ParseObject currentAnswer, final String questionId) {
        Log.d(TAG, "downvoteAnswer");
        ParseUser currentUser = ParseUser.getCurrentUser();
        List<ParseObject> answers = currentUser.getList("upvotedAnswerList");
        for (ParseObject answer : answers) {
            if (answer.getObjectId().equals(currentAnswer.getObjectId())) {
                // remove the answer from the user's upvotedAnswerList
                // decrement the upvote count on the answer
                answers.remove(answer);
                currentUser.put("upvotedAnswerList", answers);
                currentUser.saveInBackground();

                currentAnswer.increment("upvoteCount", -1);
                currentAnswer.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d(TAG, "Answer upvote decrement, for now pull questions");
                            pullSingleQuestion(questionId);
//                            if (getDelegate() != null) {
//                                getDelegate().onAnswerVoteUpdate();
//                            }
                        } else {
                            Log.d(TAG, "Answer upvote decrement Error");
                        }
                    }
                });

                return;
            }
        }
    }

    /*
     *
     */

    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate.get();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }
}
