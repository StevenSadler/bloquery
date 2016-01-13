package com.stevensadler.android.bloquery.test;

import com.stevensadler.android.bloquery.api.model.Question;

/**
 * Created by Steven on 1/3/2016.
 */
public class TestQuestionCreator {

    public TestQuestionCreator() {}

    public void addQuestion(String body) {
        Question testQuestion = new Question();
        testQuestion.setBody(body);
        testQuestion.saveInBackground();
    }
}
