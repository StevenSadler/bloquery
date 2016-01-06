package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Question;
import com.stevensadler.android.bloquery.ui.adapter.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 1/3/2016.
 */
public class QuestionListFragment extends Fragment implements
        QuestionAdapter.DataSource,
        QuestionAdapter.Delegate {

    private static final String BUNDLE_EXTRA_QUESTION = QuestionListFragment.class.getCanonicalName().concat("EXTRA_QUESTION");
    private static String TAG = QuestionListFragment.class.getSimpleName();

    public static QuestionListFragment fragmentForQuestions(List<Question> questions) {
        Bundle arguments = new Bundle();
        arguments.putLong(BUNDLE_EXTRA_QUESTION, 0l);
        QuestionListFragment questionListFragment = new QuestionListFragment();
        questionListFragment.setArguments(arguments);
        return questionListFragment;
    }

    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private List<Question> currentQuestions = new ArrayList<Question>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        questionAdapter = new QuestionAdapter();
        questionAdapter.setDataSource(this);
        questionAdapter.setDelegate(this);

//        recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_fragment_question_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(questionAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View inflate = inflater.inflate(R.layout.fragment_question_list, container, false);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_fragment_question_list);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(questionAdapter);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(questionAdapter);
    }

    /*
     * QuestionAdapter.DataSource
     */

//    @Override
//    public RssItem getRssItem(ItemAdapter itemAdapter, int position) {
//        return currentItems.get(position);
//    }
//
//    @Override
//    public RssFeed getRssFeed(ItemAdapter itemAdapter, int position) {
//        return currentFeed;
//    }

    @Override
    public int getItemCount(QuestionAdapter questionAdapter) {
        return currentQuestions.size();
    }

    /*
     * QuestionAdapter.Delegate
     */

    @Override
    public void onQuestionClicked(QuestionAdapter questionAdapter, Question question) {
        //
    }
}
