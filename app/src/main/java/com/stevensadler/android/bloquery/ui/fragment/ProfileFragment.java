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

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;

/**
 * Created by Steven on 2/7/2016.
 */
public class ProfileFragment extends Fragment implements
        View.OnClickListener {

    private static String TAG = ProfileFragment.class.getSimpleName();

    private TextView mUserNameView;
    private TextView mDescriptionText;
    private ImageView mImageView;
    private Button mExitButton;

    private String mUserId;
    private String mUserName;
    private String mUserProfileDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mUserId = getArguments().getString("userObjectId");
        mUserName = getArguments().getString("userName");
        mUserProfileDescription = getArguments().getString("userProfileDescription");

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
        mExitButton.setOnClickListener(this);

        mUserNameView.setText(mUserName);
        mDescriptionText.setText(mUserProfileDescription);
        Bitmap bitmap = BloqueryApplication.getSharedDataSource().getUserProfileImage(mUserId);
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
//        }
        mImageView.setImageBitmap(bitmap);
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
