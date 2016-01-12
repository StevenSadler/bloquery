package com.stevensadler.android.bloquery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Answer;

import java.util.List;

/**
 * Created by Steven on 1/12/2016.
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerHolder> {

    private String TAG = AnswerAdapter.class.getSimpleName();

    private List<Answer> answers;

    public AnswerAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.answer_item, parent, false);
        return new AnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerHolder holder, int position) {
        holder.update(answers.get(position));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    /*
     * AnswerHolder class
     */

    class AnswerHolder extends RecyclerView.ViewHolder { // implements View.OnClickListener {

        public TextView answerTextView;

        private Answer answer;

        public AnswerHolder(View itemView) {
            super(itemView);
            answerTextView = (TextView) itemView.findViewById(R.id.tv_answer_item_body);
            //answerTextView.setOnClickListener(this);
        }

        void update(Answer answer) {
            Log.d(TAG, "update");
            this.answer = answer;

            String text = answer.getBody();
            answerTextView.setText(text);
        }

//        @Override
//        public void onClick(View v) {
//            //
//        }
    }
}
