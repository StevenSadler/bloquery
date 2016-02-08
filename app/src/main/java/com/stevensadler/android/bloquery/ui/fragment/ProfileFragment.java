package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;

/**
 * Created by Steven on 2/7/2016.
 */
public class ProfileFragment extends Fragment implements
        View.OnClickListener {

    private static String TAG = ProfileFragment.class.getSimpleName();

    private TextView mUserNameView;
    private ImageView mImageView;
    private Button mExitButton;

    private Bitmap mBitmap;
    private TextView mDescriptionText;
    private String mUserId;
    private String mUserProfileDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mUserId = getArguments().getString("userObjectId");
        mUserProfileDescription = getArguments().getString("userProfileDescription");

        // TODO finish this, in onCreateView get the user by the userObjectId, get the imagefile, get the bitmap


//        ParseUser
//
//        mQuestion = BloqueryApplication.getSharedDataSource().getQuestionById(questionId);
//        List<ParseObject> answers = mQuestion.getList("answerList");
//        mAnswerAdapter = new AnswerAdapter(answers);
//        mAnswerAdapter.setDelegate(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_profile, viewGroup, false);
        mUserNameView = (TextView) view.findViewById(R.id.tv_fragment_profile_user);
        mDescriptionText = (TextView) view.findViewById(R.id.tv_fragment_profile_description);
        mImageView = (ImageView) view.findViewById(R.id.iv_fragment_profile_image);
        mExitButton = (Button) view.findViewById(R.id.b_fragment_profile_exit);

        // TODO get name from DataSource instead of calling ParseUser
        mUserNameView.setText(ParseUser.getCurrentUser().getUsername());
        mExitButton.setOnClickListener(this);

        mDescriptionText.setText(BloqueryApplication.getSharedDataSource().getCurrentUserProfileDescription());
        mBitmap = BloqueryApplication.getSharedDataSource().getCurrentUserProfileImage();
        if (mBitmap != null) {
            mImageView.setImageBitmap(mBitmap);
        }
        return view;
    }

    /*
     * View.OnClickListener
     */
    @Override
    public void onClick(View view) {
        if (view == mExitButton) {
            Log.d(TAG, "onClick mExitButton");
            getFragmentManager().popBackStack();
        }
    }
}
