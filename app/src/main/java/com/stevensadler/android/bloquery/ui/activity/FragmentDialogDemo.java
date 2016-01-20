package com.stevensadler.android.bloquery.ui.activity;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.fragment.AddQuestionDialogFragment;

/**
 * Created by Steven on 1/18/2016.
 */
public class FragmentDialogDemo extends FragmentActivity implements AddQuestionDialogFragment.AddQuestionDialogListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloquery);
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddQuestionDialogFragment addQuestionDialog = new AddQuestionDialogFragment();
        addQuestionDialog.show(fragmentManager, "fragment_add_question");
    }

    @Override
    public void onFinishAddQuestionDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_LONG).show();
    }
}
