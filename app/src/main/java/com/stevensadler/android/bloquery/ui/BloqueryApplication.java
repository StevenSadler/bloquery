package com.stevensadler.android.bloquery.ui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.api.model.DataSource;
import com.stevensadler.android.bloquery.network.NetworkManager;

/**
 * Created by Steven on 12/29/2015.
 */
public class BloqueryApplication extends Application {

    public static BloqueryApplication getSharedInstance() {
        return sharedInstance;
    }

    public static DataSource getSharedDataSource() {
        return BloqueryApplication.getSharedInstance().getDataSource();
    }

    public static NetworkManager getSharedNetworkManager() {
        return BloqueryApplication.getSharedInstance().getNetworkManager();
    }

    private static BloqueryApplication sharedInstance;
    private NetworkManager mNetworkManager;
    private DataSource mDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        //Parse.enableLocalDatastore(this);

        //ParseObject.registerSubclass(Question.class);
        //ParseObject.registerSubclass(Answer.class);
        Parse.initialize(this);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // if you would like all objects to be private by default, remove this line
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);


        mDataSource = new DataSource();
        mNetworkManager = new NetworkManager();
        mNetworkManager.setDelegate(mDataSource);
        mNetworkManager.pullQuestions();
    }

    private DataSource getDataSource() {
        return mDataSource;
    }

    private NetworkManager getNetworkManager() {
        return mNetworkManager;
    }
}
