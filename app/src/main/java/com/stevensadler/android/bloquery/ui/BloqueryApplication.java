package com.stevensadler.android.bloquery.ui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.api.model.Question;

/**
 * Created by Steven on 12/29/2015.
 */
public class BloqueryApplication extends Application {

    public static BloqueryApplication getSharedInstance() {
        return sharedInstance;
    }

    private static BloqueryApplication sharedInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Question.class);
        Parse.initialize(this);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // if you would like all objects to be private by default, remove this line
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
