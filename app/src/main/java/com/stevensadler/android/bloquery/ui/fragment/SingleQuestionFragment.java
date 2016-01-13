package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Answer;
import com.stevensadler.android.bloquery.api.model.ParseProxyObject;
import com.stevensadler.android.bloquery.api.model.Question;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 1/9/2016.
 */
public class SingleQuestionFragment extends Fragment implements View.OnClickListener { //}, QuestionAdapter.Delegate {

    public static interface Delegate {
        public void onQuestionClicked(SingleQuestionFragment singleQuestionFragment, Question question);
        public void onSubmitAnswerClicked(SingleQuestionFragment singleQuestionFragment, Answer answer);
    }

    private static String TAG = SingleQuestionFragment.class.getSimpleName();


    private Question mQuestion;
    private TextView mTextView;
    private EditText mEditText;
    private Button mSubmitButton;

    private WeakReference<Delegate> delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        List<Question> questions = new ArrayList<Question>();

        ParseProxyObject parseProxyObject = (ParseProxyObject) getArguments().getSerializable("questionObject");
        mQuestion = new Question();
        mQuestion.setBody(parseProxyObject.getString("body"));
        mQuestion.setQuestionId(parseProxyObject.getString("questionId"));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_single_question, viewGroup, false);
        mTextView = (TextView) view.findViewById(R.id.tv_fragment_single_question);
        mEditText = (EditText) view.findViewById(R.id.et_fragment_single_question_answer);
        mSubmitButton = (Button) view.findViewById(R.id.b_fragment_single_question_submit_answer);

        mTextView.setText(mQuestion.getBody());
        mTextView.setOnClickListener(this);
        mEditText.setText("");
        mSubmitButton.setOnClickListener(this);
        return view;
    }

    /*
     * View.OnClickListener
     */
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        if (view == mTextView) {
            if (getDelegate() != null) {
                getDelegate().onQuestionClicked(SingleQuestionFragment.this, mQuestion);
            }
        } else if (view == mSubmitButton) {

            if (getDelegate() != null) {
                Answer answer = new Answer();
                answer.setQuestionId(mQuestion.getQuestionId());
                answer.setBody(mEditText.getText().toString());
                answer.saveInBackground();
                getDelegate().onSubmitAnswerClicked(SingleQuestionFragment.this, answer);
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

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }
}
