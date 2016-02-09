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
 * Created by Steven on 1/7/2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    public static interface Delegate {
        public void onQuestionClicked(QuestionAdapter questionAdapter, ParseObject question);
        public void onUserProfileImageClicked(ParseUser parseUser);
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
        public ImageView questionAuthorImageView;

        private ParseObject mQuestion;

        public QuestionHolder(View itemView) {
            super(itemView);
            questionTextView = (TextView) itemView.findViewById(R.id.tv_question_item_body);
            questionTextView.setOnClickListener(this);
            questionAuthorImageView = (ImageView) itemView.findViewById(R.id.iv_question_item_image);
            questionAuthorImageView.setOnClickListener(this);
        }

        void update(ParseObject question) {
            mQuestion = question;
            String text = question.getString("body");
            questionTextView.setText(text);

            // use test image until i can get question author's profile image
            //questionAuthorImageView.setImageBitmap(BloqueryApplication.getSharedDataSource().getCurrentUserProfileImage());

            ParseUser questionAuthor = question.getParseUser("createdBy");
            Bitmap bitmap = BloqueryApplication.getSharedDataSource().getUserProfileImage(questionAuthor.getObjectId());
            questionAuthorImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            if (view == questionTextView) {
                if (getDelegate() != null) {
                    getDelegate().onQuestionClicked(QuestionAdapter.this, mQuestion);
                }
            } else if (view == questionAuthorImageView) {
                Log.d(TAG, "onClick on questionAuthorImageView");
                if (getDelegate() != null) {
                    getDelegate().onUserProfileImageClicked(mQuestion.getParseUser("createdBy"));
                }
            }
        }
    }
}
