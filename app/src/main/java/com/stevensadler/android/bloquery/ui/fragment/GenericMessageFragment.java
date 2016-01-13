package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;

import java.lang.ref.WeakReference;

/**
 * Created by Steven on 1/10/2016.
 */
public class GenericMessageFragment extends Fragment implements View.OnClickListener {

    public static interface Delegate {
        public void onGenericMessageClicked(GenericMessageFragment genericMessageFragment, String message);
    }

    private static String TAG = GenericMessageFragment.class.getSimpleName();

    private TextView mTextView;
    private String mGenericMessage;

    private WeakReference<Delegate> delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        mGenericMessage = getArguments().getString("message");
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_generic_message, viewGroup, false);
        mTextView = (TextView) view.findViewById(R.id.tv_fragment_generic_message);
        mTextView.setText(mGenericMessage);
        mTextView.setOnClickListener(this);
        return view;
    }

    /*
     * View.OnClickListener
     */
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        if (getDelegate() != null) {
            getDelegate().onGenericMessageClicked(GenericMessageFragment.this, mGenericMessage);
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

