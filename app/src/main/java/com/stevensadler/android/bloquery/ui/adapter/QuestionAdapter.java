package com.stevensadler.android.bloquery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.api.model.Question;

import java.lang.ref.WeakReference;

/**
 * Created by Steven on 1/3/2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionAdapterViewHolder> {

    public static interface DataSource {
        public int getItemCount(QuestionAdapter questionAdapter);
    }

    public static interface Delegate {
        public void onQuestionClicked(QuestionAdapter questionAdapter, Question question);
    }

    private static String TAG = QuestionAdapter.class.getSimpleName();

    private WeakReference<Delegate> delegate;
    private WeakReference<DataSource> dataSource;

    public QuestionAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item, viewGroup, false);
        return new QuestionAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(QuestionAdapterViewHolder questionAdapterViewHolder, int index) {
        //
    }

    @Override
    public int getItemCount() {
        if (getDataSource() == null) {
            return 0;
        }
        return getDataSource().getItemCount(this);
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            return null;
        }
        return dataSource.get();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = new WeakReference<DataSource>(dataSource);
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

    class QuestionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView body;

        Question question;

        public QuestionAdapterViewHolder(View questionView) {
            super(questionView);
            body = (TextView) questionView.findViewById(R.id.tv_question_item_body);

            questionView.setOnClickListener(this);
        }

        @Override
        public void onClick(View veiw) {
            if (getDelegate() != null) {
                getDelegate().onQuestionClicked(QuestionAdapter.this, question);
            }
        }
    }
}
