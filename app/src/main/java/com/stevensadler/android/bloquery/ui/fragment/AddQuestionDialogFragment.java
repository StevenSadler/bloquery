package com.stevensadler.android.bloquery.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stevensadler.android.bloquery.R;

/**
 * Created by Steven on 1/17/2016.
 */
public class AddQuestionDialogFragment extends DialogFragment implements
        View.OnClickListener,
        TextView.OnEditorActionListener {

    public interface AddQuestionDialogListener {
        void onFinishAddQuestionDialog(String inputText);
    }

    private EditText mEditText;
    private Button mClearButton;

    public AddQuestionDialogFragment() {
        // empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_add_question, viewGroup);
        mEditText = (EditText) view.findViewById(R.id.et_input_question);
        mClearButton = (Button) view.findViewById(R.id.b_add_question_clear);
        getDialog().setTitle("Hello");

        mClearButton.setOnClickListener(this);

        // show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // return input text to activity
            AddQuestionDialogListener activity = (AddQuestionDialogListener) getActivity();
            activity.onFinishAddQuestionDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == mClearButton) {
            mEditText.setText("");
        }
    }
}
