package com.stevensadler.android.bloquery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.stevensadler.android.bloquery.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Steven on 1/7/2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    public static interface Delegate {
        public void onQuestionClicked(QuestionAdapter questionAdapter, ParseObject question);
    }

    private String TAG = QuestionAdapter.class.getSimpleName();

    private List<ParseObject> mQuestions;
    private WeakReference<Delegate> delegate;

    public QuestionAdapter(List<ParseObject> questions) {
        mQuestions = questions;
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.question_item, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionHolder holder, int position) {
        holder.update(mQuestions.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
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




    /*
     * QuestionHolder class
     */

    class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView questionTextView;

        private ParseObject mQuestion;

        public QuestionHolder(View itemView) {
            super(itemView);
            questionTextView = (TextView) itemView.findViewById(R.id.tv_question_item_body);
            questionTextView.setOnClickListener(this);
        }

        void update(ParseObject question) {
            mQuestion = question;
            String text = question.getString("body");
            questionTextView.setText(text);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            if (getDelegate() != null) {
                getDelegate().onQuestionClicked(QuestionAdapter.this, mQuestion);
            }
        }
    }
}
