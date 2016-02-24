package com.stevensadler.android.bloquery.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
        public void onAnswerUpvoteClicked(ParseObject answer);
        public void onAnswerDownvoteClicked(ParseObject answer);
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
        //int reversedOrderIndex = getItemCount() - 1 - position;
        //holder.update(answers.get(reversedOrderIndex));

        holder.update(answers.get(position));
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

//    /*
//     * private helper functions
//     */
//    private List<ParseObject> sortByDescendingUpvotes(List<ParseObject> answers) {
//        List<ParseObject> sortedAnswers = answers;
//        Collections.sort(sortedAnswers, new Comparator<ParseObject>() {
//
//            @Override
//            public int compare(ParseObject object1, ParseObject object2) {
//                Log.d(TAG, "object1.getInt(upvoteCount) = " + object1.getInt("upvoteCount") + " object2.getInt(upvoteCount) = " + object2.getInt("upvoteCount"));
//                return object2.getInt("upvoteCount") - object1.getInt("upvoteCount");
//            }
//        });
//        return sortedAnswers;
//    }

    /*
     * AnswerHolder class
     */

    class AnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView answerTextView;
        public ImageView answerAuthorImageView;
        public CheckBox answerUpvoteCheckbox;
        public TextView answerUpvoteCount;

        private ParseObject answer;
        private int upvoteCount;


        public AnswerHolder(View itemView) {
            super(itemView);
            answerTextView = (TextView) itemView.findViewById(R.id.tv_answer_item_body);
            //answerTextView.setOnClickListener(this);

            answerAuthorImageView = (ImageView) itemView.findViewById(R.id.iv_answer_item_image);
            answerAuthorImageView.setOnClickListener(this);

            answerUpvoteCheckbox = (CheckBox) itemView.findViewById(R.id.cb_answer_favorite_star);
            answerUpvoteCheckbox.setOnClickListener(this);

            answerUpvoteCount = (TextView) itemView.findViewById(R.id.tv_answer_vote_count);
        }

        void update(ParseObject answer) {
            Log.d(TAG, "update");
            this.answer = answer;

            String text = answer.getString("body");
            answerTextView.setText(text);

            ParseUser answerAuthor = answer.getParseUser("createdBy");
            Bitmap bitmap = BloqueryApplication.getSharedDataSource().getUserProfileImage(answerAuthor.getObjectId());
            answerAuthorImageView.setImageBitmap(bitmap);

            upvoteCount = (int) answer.getNumber("upvoteCount");
            answerUpvoteCount.setText("" + upvoteCount);
            Boolean voted = BloqueryApplication.getSharedDataSource().didUserVoteOnAnswer(ParseUser.getCurrentUser(), answer);
            Log.d(TAG, "update - answer vote " + voted + "    answer body - " + answer.getString("body"));
            answerUpvoteCheckbox.setChecked(voted);


        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            if (view == answerAuthorImageView) {
                Log.d(TAG, "onClick on answerAuthorImageView");
                if (getDelegate() != null) {
                    getDelegate().onUserProfileImageClicked(this.answer.getParseUser("createdBy"));
                }
            } else if (view == answerUpvoteCheckbox) {
                if (answerUpvoteCheckbox.isChecked()) {
                    Log.d(TAG, "onClick on answerUpvoteCheckbox IS checked");
                    if (getDelegate() != null) {
                        getDelegate().onAnswerUpvoteClicked(this.answer);
                        upvoteCount++;
                        answerUpvoteCount.setText("" + upvoteCount);
                    }

                } else {
                    Log.d(TAG, "onClick on answerUpvoteCheckbox IS NOT checked");
                    if (getDelegate() != null) {
                        getDelegate().onAnswerDownvoteClicked(this.answer);
                        upvoteCount--;
                        answerUpvoteCount.setText("" + upvoteCount);
                    }
                }
            }
        }
    }
}
