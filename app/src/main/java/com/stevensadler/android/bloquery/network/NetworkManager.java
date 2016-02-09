package com.stevensadler.android.bloquery.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.FindCallback;
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
        answer.saveInBackground();

        question.add("answerList", answer);
        question.saveInBackground();
    }

    //public void saveProfile(final ParseObject profile, Bitmap bitmap, String description) {
    public void saveProfile(Bitmap bitmap, String description) {

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] image = stream.toByteArray();
//
//        ParseFile parseFile = new ParseFile("user.jpeg", image);
//
//        profile.put("description", description);
//        profile.put("imageFile", parseFile);
//        profile.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d(TAG, "Profile edited");
//                    if (getDelegate() != null) {
//                        getDelegate().onPullCurrentUserProfile(profile);
//                    }
//                } else {
//                    Log.d(TAG, "Profile edit Error");
//                }
//            }
//        });

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
