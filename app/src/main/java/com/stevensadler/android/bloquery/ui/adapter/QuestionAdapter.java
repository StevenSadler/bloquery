package com.stevensadler.android.bloquery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Question;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Steven on 1/7/2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    public static interface Delegate {
        public void onQuestionClicked(QuestionAdapter questionAdapter, Question question);
    }

    private String TAG = QuestionAdapter.class.getSimpleName();

    private List<Question> questions;
    private WeakReference<Delegate> delegate;

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
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
        holder.update(questions.get(position));

        Log.d(TAG, "onBindViewHolder " + position);
        Log.d(TAG, "breakpoint");
    }

    @Override
    public int getItemCount() {
        return questions.size();
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

        private Question question;

        public QuestionHolder(View itemView) {
            super(itemView);
            questionTextView = (TextView) itemView.findViewById(R.id.tv_question_item_body);
            questionTextView.setOnClickListener(this);
        }

        void update(Question question) {
            Log.d(TAG, "update");
            this.question = question;

            String text = question.getBody();
            questionTextView.setText(text);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            if (getDelegate() != null) {
                getDelegate().onQuestionClicked(QuestionAdapter.this, question);
            }
        }
    }
}
