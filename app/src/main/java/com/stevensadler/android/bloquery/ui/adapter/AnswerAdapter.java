package com.stevensadler.android.bloquery.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Steven on 1/12/2016.
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerHolder> {

    public static interface Delegate {
        public void onUserProfileImageClicked(ParseUser parseUser);
    }

    private String TAG = AnswerAdapter.class.getSimpleName();

    private List<ParseObject> answers;
    private WeakReference<Delegate> delegate;

    public AnswerAdapter(List<ParseObject> answers) {
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
        int reversedOrderIndex = getItemCount() - 1 - position;
        holder.update(answers.get(reversedOrderIndex));
    }

    @Override
    public int getItemCount() {
        return answers.size();
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
     * AnswerHolder class
     */

    class AnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView answerTextView;
        public ImageView answerAuthorImageView;

        private ParseObject answer;

        public AnswerHolder(View itemView) {
            super(itemView);
            answerTextView = (TextView) itemView.findViewById(R.id.tv_answer_item_body);
            //answerTextView.setOnClickListener(this);

            answerAuthorImageView = (ImageView) itemView.findViewById(R.id.iv_answer_item_image);
            answerAuthorImageView.setOnClickListener(this);
        }

        void update(ParseObject answer) {
            Log.d(TAG, "update");
            this.answer = answer;

            String text = answer.getString("body");
            answerTextView.setText(text);

            ParseUser answerAuthor = answer.getParseUser("createdBy");
            Bitmap bitmap = BloqueryApplication.getSharedDataSource().getUserProfileImage(answerAuthor);
            answerAuthorImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            if (view == answerAuthorImageView) {
                Log.d(TAG, "onClick on answerAuthorImageView");
                if (getDelegate() != null) {
                    getDelegate().onUserProfileImageClicked(this.answer.getParseUser("createdBy"));
                }
            }
        }
    }
}
