package com.stevensadler.android.bloquery.test;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Steven on 1/3/2016.
 */
public class TestQuestionCreator {

    public TestQuestionCreator() {}

    public void addQuestion(String body) {
        ParseObject question = ParseObject.create("Question");
        question.put("body", body);
        question.put("createdBy", ParseUser.getCurrentUser());
        //question.put("user", ParseUser.getCurrentUser());
        question.put("answerList", new ArrayList<ParseObject>());
        question.saveInBackground();
    }
}
