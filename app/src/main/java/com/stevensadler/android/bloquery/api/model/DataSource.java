package com.stevensadler.android.bloquery.api.model;

import android.util.Log;

import com.parse.ParseObject;
import com.stevensadler.android.bloquery.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Steven on 1/12/2016.
 */
public class DataSource extends Observable implements NetworkManager.Delegate {

    private String TAG = DataSource.class.getSimpleName();

    private List<ParseObject> mQuestions;

    public DataSource() {
        mQuestions = new ArrayList<ParseObject>();
    }

    public List<ParseObject> getQuestions() {
        return mQuestions;
    }

    public ParseObject getQuestionById(String questionObjectId) {
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
    public void onPullQuestions(List<ParseObject> objects) {
        Log.d(TAG, "Retrieved " + objects.size() + " objects");
        for (ParseObject object :objects) {
            Log.d(TAG, object.getObjectId() + " " + object.getString("body"));
        }
        mQuestions = objects;

        setChanged();
        notifyObservers();
        clearChanged();
    }
}
