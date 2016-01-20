package com.stevensadler.android.bloquery.network;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 1/14/2016.
 */
public class NetworkManager {

    public interface Delegate {
        public void onPullQuestions(List<ParseObject> objects);
    }

    private String TAG = NetworkManager.class.getSimpleName();

    private WeakReference<Delegate> delegate;

    public NetworkManager() {}

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
