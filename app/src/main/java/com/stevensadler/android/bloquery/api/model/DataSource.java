package com.stevensadler.android.bloquery.api.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.stevensadler.android.bloquery.network.NetworkManager;
import com.stevensadler.android.bloquery.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Steven on 1/12/2016.
 */
public class DataSource extends Observable implements
        NetworkManager.Delegate {

    private String TAG = DataSource.class.getSimpleName();

    private List<ParseObject> mQuestions;
    private ParseObject mCurrentUserProfile;
    private Bitmap mCurrentUserProfileImage;

    public DataSource() {
        mQuestions = new ArrayList<ParseObject>();
    }

    public List<ParseObject> getQuestions() {
        return mQuestions;
    }
    public ParseObject getCurrentUserProfile() {
        return mCurrentUserProfile;
    }
    public String getCurrentUserDescription() {
        return mCurrentUserProfile.getString("description");
    }
    public Bitmap getCurrentUserProfileImage() {
        return mCurrentUserProfileImage;
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

    @Override
    public void onPullCurrentUserProfile(ParseObject object) {
        Log.d(TAG, "Retrieved User Profile " + object.getString("description"));

        mCurrentUserProfile = object;
        ParseFile imageFile = object.getParseFile("imageFile");
        if (imageFile != null) {
            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapUtils.byteArrayToBitmap(data);
                        if (bitmap == null) {
                            Log.d(TAG, "bitmap built from user profile imageFile data IS null");
                            //mCurrentUserProfileImage = null;
                        } else {
                            Log.d(TAG, "bitmap built from user profile imageFile data IS NOT null");
                            mCurrentUserProfileImage = bitmap;
                        }
                    } else {
                        Log.d(TAG, "parse Error: " + e.getMessage());
                    }
                }
            });
        }



//        ParseFile imageFile = (ParseFile) object.get("imageFile");
//        setChanged();
//        notifyObservers();
//        clearChanged();
    }
}
