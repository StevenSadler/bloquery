package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;
import com.stevensadler.android.bloquery.ui.adapter.QuestionAdapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Steven on 1/7/2016.
 */
public class QuestionListFragment extends Fragment implements
        IDelegatingFragment,
        Observer,
        QuestionAdapter.Delegate {

    public static interface Delegate extends IFragmentDelegate {
        public void onQuestionListClicked(ParseObject question);
        //public void setDelegate(Delegate delegate);
    }

    private static String TAG = QuestionListFragment.class.getSimpleName();

    private static List<ParseObject> mQuestions;

    private RecyclerView mRecyclerView;
    private QuestionAdapter mQuestionAdapter;

    private WeakReference<Delegate> delegate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestions = BloqueryApplication.getSharedDataSource().getQuestions();

        mQuestionAdapter = new QuestionAdapter(mQuestions);
        mQuestionAdapter.setDelegate(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_question_list, viewGroup, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_question_list);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mQuestionAdapter);
        return view;
    }

    /*
     * Observer
     */
    @Override
    public void update(Observable observable, Object data) {
        Log.d(TAG, "update");

        List<ParseObject> questions = BloqueryApplication.getSharedDataSource().getQuestions();
        for (ParseObject question : questions) {
            Log.d(TAG, question.getString("body"));
        }
        mQuestions = questions;
        mQuestionAdapter = new QuestionAdapter(mQuestions);
        mQuestionAdapter.setDelegate(this);
        mRecyclerView.setAdapter(mQuestionAdapter);
    }

    /*
     * QuestionAdapter.Delegate
     */
    @Override
    public void onQuestionClicked(QuestionAdapter questionAdapter, ParseObject question) {
        Log.d(TAG, "onQuestionClicked: " + question.getString("body"));
        if (getDelegate() != null) {
            Log.d(TAG, "onQuestionClicked: call delegate click handler");
            getDelegate().onQuestionListClicked(question);
        }
        Log.d(TAG, "onQuestionClicked: end");
    }

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
}
