package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;
import com.stevensadler.android.bloquery.ui.adapter.AnswerAdapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Steven on 1/9/2016.
 */
public class SingleQuestionFragment extends Fragment implements
        IDelegatingFragment,
        Observer,
        AnswerAdapter.Delegate,
        View.OnClickListener {

    public static interface Delegate extends IFragmentDelegate {
        public void onSingleQuestionClicked(ParseObject question);
        public void onSubmitAnswerClicked(ParseObject question, String answerBody);
        public void onUserProfileImageClicked(ParseUser parseUser);
    }

    private static String TAG = SingleQuestionFragment.class.getSimpleName();

    private ParseObject mQuestion;
    private ImageView mQuestionAuthorImageView;
    private TextView mTextView;
    private EditText mEditText;
    private Button mSubmitButton;

    private RecyclerView mRecyclerView;
    private AnswerAdapter mAnswerAdapter;

    private WeakReference<Delegate> delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        String questionId = getArguments().getString("questionObjectId");

        mQuestion = BloqueryApplication.getSharedDataSource().getQuestionById(questionId);
        List<ParseObject> answers = mQuestion.getList("answerList");
        mAnswerAdapter = new AnswerAdapter(answers);
        mAnswerAdapter.setDelegate(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_single_question, viewGroup, false);
        mQuestionAuthorImageView = (ImageView) view.findViewById(R.id.iv_fragment_single_question_image);
        mTextView = (TextView) view.findViewById(R.id.tv_fragment_single_question);
        mEditText = (EditText) view.findViewById(R.id.et_fragment_single_question_answer);
        mSubmitButton = (Button) view.findViewById(R.id.b_fragment_single_question_submit_answer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_single_question_answer_list);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAnswerAdapter);

        ParseUser questionAuthor = mQuestion.getParseUser("createdBy");
        //Log.d(TAG, "create single question for user " + questionAuthor.getUsername());

        Bitmap bitmap = BloqueryApplication.getSharedDataSource().getUserProfileImage(questionAuthor);
        mQuestionAuthorImageView.setImageBitmap(bitmap);

        mTextView.setText(mQuestion.getString("body"));
        mTextView.setOnClickListener(this);
        mEditText.setText("");
        mSubmitButton.setOnClickListener(this);
        mQuestionAuthorImageView.setOnClickListener(this);
        return view;
    }

    /*
     * View.OnClickListener
     */
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        if (view == mTextView) {
            Log.d(TAG, "onClick mTextView");
            if (getDelegate() != null) {
                getDelegate().onSingleQuestionClicked(mQuestion);
            }
        } else if (view == mSubmitButton) {
            Log.d(TAG, "onClick mSubmitButton");
            if (getDelegate() != null) {

                String answerBody = mEditText.getText().toString();
                getDelegate().onSubmitAnswerClicked(mQuestion, answerBody);
            }
        } else if (view == mQuestionAuthorImageView) {
            Log.d(TAG, "onClick mQuestionAuthorImageView");
            if (getDelegate() != null) {
                getDelegate().onUserProfileImageClicked(mQuestion.getParseUser("createdBy"));
            }
        }
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

    /*
     * IDelegatingFragment
     */
    public void setDelegate(IFragmentDelegate iFragmentDelegate) {
        if (iFragmentDelegate != null) {
            Delegate delegate = (Delegate) iFragmentDelegate;
            if (delegate != null) {
                this.delegate = new WeakReference<Delegate>(delegate);
            }
        }
    }

    /*
     * Observer
     */
    @Override
    public void update(Observable observable, Object data) {
        Log.d(TAG, "update");

        String oldQuestionId = mQuestion.getObjectId();
        ParseObject question = BloqueryApplication.getSharedDataSource().getQuestionById(oldQuestionId);

        // if old question is still in the new pull query result
        // then refresh the view, otherwise ignore the update
        if (question != null) {
            mQuestion = question;
            List<ParseObject> answers = mQuestion.getList("answerList");
            mAnswerAdapter = new AnswerAdapter(answers);
            mAnswerAdapter.setDelegate(this);
            mRecyclerView.setAdapter(mAnswerAdapter);
        }
    }

    /*
     * AnswerAdapter.Delegate
     */
    @Override
    public void onUserProfileImageClicked(ParseUser parseUser) {
        Log.d(TAG, "onUserProfileImageClicked");
        if (getDelegate() != null) {
            getDelegate().onUserProfileImageClicked(parseUser);
        }
    }
}
