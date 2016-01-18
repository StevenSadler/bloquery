package com.stevensadler.android.bloquery.network;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
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
                    Log.d(TAG, "test parse query: Error: " + e.getMessage());
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
