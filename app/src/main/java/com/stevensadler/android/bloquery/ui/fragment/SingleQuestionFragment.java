package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;
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
    }

    private static String TAG = SingleQuestionFragment.class.getSimpleName();

    private TextView mTextView;
    private Question mQuestion;
    //private QuestionAdapter mQuestionAdapter;

    private WeakReference<Delegate> delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        List<Question> questions = new ArrayList<Question>();

        ParseProxyObject parseProxyObject = (ParseProxyObject) getArguments().getSerializable("questionObject");
        mQuestion = new Question();
        mQuestion.setBody(parseProxyObject.getString("body"));

        //mQuestionAdapter = new QuestionAdapter(questions);
       // mQuestionAdapter.setDelegate(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_single_question, viewGroup, false);
        mTextView = (TextView) view.findViewById(R.id.tv_fragment_single_question);
        mTextView.setText(mQuestion.getBody());
        mTextView.setOnClickListener(this);
        return view;
    }

    /*
     * View.OnClickListener
     */
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        if (getDelegate() != null) {
            getDelegate().onQuestionClicked(SingleQuestionFragment.this, mQuestion);
        }
    }

//    /*
//     * QuestionAdapter.Delegate
//     */
//    @Override
//    public void onQuestionClicked(QuestionAdapter questionAdapter, Question question) {
//        Log.d(TAG, "onQuestionClicked: " + question.getBody());
//        if (getDelegate() != null) {
//            getDelegate().onQuestionClicked(SingleQuestionFragment.this, question);
//        }
//    }

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
