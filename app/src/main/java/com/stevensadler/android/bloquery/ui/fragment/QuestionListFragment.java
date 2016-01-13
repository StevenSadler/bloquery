package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.ParseProxyObject;
import com.stevensadler.android.bloquery.api.model.Question;
import com.stevensadler.android.bloquery.ui.adapter.QuestionAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 1/7/2016.
 */
public class QuestionListFragment extends Fragment implements QuestionAdapter.Delegate {

    public static interface Delegate {
        public void onQuestionClicked(QuestionListFragment questionListFragment, Question question);
    }

    //private static final String BUNDLE_QUESTIONS = QuestionListFragment.class.getCanonicalName().concat(".BUNDLE_QUESTIONS");
    private static String TAG = QuestionListFragment.class.getSimpleName();

    private static List<Question> mQuestions;

    private RecyclerView mRecyclerView;
    private QuestionAdapter mQuestionAdapter;

    private WeakReference<Delegate> delegate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestions = new ArrayList<Question>();

        int size = getArguments().getInt("size");
        for (int index = 0; index < size; index++) {
            ParseProxyObject parseProxyObject = (ParseProxyObject) getArguments().getSerializable("" + index);
            Question question = new Question();
            question.setBody(parseProxyObject.getString("body"));
            question.setQuestionId(parseProxyObject.getString("questionId"));
            //question.setObjectId(parseProxyObject.getString("objectId"));
            mQuestions.add(question);

            Log.d(TAG, "onCreate  index = " + index + " " + parseProxyObject.getString("questionId") + " " + question.getBody());
        }
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
     * QuestionAdapter.Delegate
     */
    @Override
    public void onQuestionClicked(QuestionAdapter questionAdapter, Question question) {
        Log.d(TAG, "onQuestionClicked: " + question.getBody());
        if (getDelegate() != null) {
            getDelegate().onQuestionClicked(QuestionListFragment.this, question);
        }
    }

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
