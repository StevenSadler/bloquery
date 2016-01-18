package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
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
        Observer,
        View.OnClickListener { //}, QuestionAdapter.Delegate {

    public static interface Delegate {
        public void onQuestionClicked(SingleQuestionFragment singleQuestionFragment, ParseObject question);
        public void onSubmitAnswerClicked(SingleQuestionFragment singleQuestionFragment, ParseObject question, String answerBody);
    }

    private static String TAG = SingleQuestionFragment.class.getSimpleName();


    private ParseObject mQuestion;
    private TextView mTextView;
    private EditText mEditText;
    private Button mSubmitButton;

    private List<ParseObject> mAnswers;
    private RecyclerView mRecyclerView;
    private AnswerAdapter mAnswerAdapter;

    private WeakReference<Delegate> delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestion = BloqueryApplication.getSharedDataSource().getSelectedQuestion();

        mAnswers = mQuestion.getList("answerList");

        // for now, test with an empty array
        //mAnswers = new ArrayList<ParseObject>();

        Log.d(TAG, "onCreate");

//        List<Question> questions = new ArrayList<Question>();
//
//        ParseProxyObject parseProxyQuestion = (ParseProxyObject) getArguments().getSerializable("questionObject");
//        mQuestion = new Question();
//        mQuestion.setBody(parseProxyQuestion.getString("body"));
//        mQuestion.setQuestionId(parseProxyQuestion.getString("questionId"));
//
//        int size = getArguments().getInt("size");
//        for (int index = 0; index < size; index++) {
//            ParseProxyObject parseProxyObject = (ParseProxyObject) getArguments().getSerializable("" + index);
//            Answer answer = new Answer();
//            answer.setBody(parseProxyObject.getString("body"));
//            answer.setQuestionId(parseProxyObject.getString("questionId"));
//            mAnswers.add(answer);
//
//            Log.d(TAG, "onCreate  index = " + index + " " + parseProxyObject.getString("questionId") + " " + mQuestion.getBody());
//        }
        mAnswerAdapter = new AnswerAdapter(mAnswers);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_single_question, viewGroup, false);
        mTextView = (TextView) view.findViewById(R.id.tv_fragment_single_question);
        mEditText = (EditText) view.findViewById(R.id.et_fragment_single_question_answer);
        mSubmitButton = (Button) view.findViewById(R.id.b_fragment_single_question_submit_answer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_single_question_answer_list);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAnswerAdapter);

        mTextView.setText(mQuestion.getString("body"));
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

                String answerBody = mEditText.getText().toString();
                getDelegate().onSubmitAnswerClicked(SingleQuestionFragment.this, mQuestion, answerBody);

//
//                Answer answer = new Answer();
//                //answer.setQuestionId(mQuestion.getQuestionId());
//                answer.setBody(mEditText.getText().toString());
//                answer.saveInBackground();
//                getDelegate().onSubmitAnswerClicked(SingleQuestionFragment.this, answer);
//
////                ParseObject answer = new ParseObject("Answer");
////                answer.put("body", mEditText.getText().toString());
////                answer.put("question", mPOQuestion);
////                answer.saveInBackground();
////                getDelegate().onSubmitAnswerClicked(SingleQuestionFragment.this, answer);
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

    /*
     * Observer
     */
    @Override
    public void update(Observable observable, Object data) {
        Log.d(TAG, "update");
//
//        List<ParseObject> questions = BloqueryApplication.getSharedDataSource().getQuestions();
//        for (ParseObject question : questions) {
//            Log.d(TAG, question.getString("body"));
//        }
//        mQuestions = questions;
//
//        mQuestionAdapter = new QuestionAdapter(mQuestions);
//        mQuestionAdapter.setDelegate(this);
//        mRecyclerView.setAdapter(mQuestionAdapter);


//        List<ParseObject> questions = (List<ParseObject>) data;
//        for (ParseObject question : questions) {
//            Log.d(TAG, question.getString("body"));
//        }
//        mQuestions = questions;
//
//        mQuestionAdapter = new QuestionAdapter(mQuestions);
//        mQuestionAdapter.setDelegate(this);
//        mRecyclerView.setAdapter(mQuestionAdapter);
    }
}
