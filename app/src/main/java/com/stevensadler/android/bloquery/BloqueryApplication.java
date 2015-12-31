package com.stevensadler.android.bloquery;

import android.app.Application;

import com.parse.Parse;

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

        Parse.initialize(this);
    }
}
