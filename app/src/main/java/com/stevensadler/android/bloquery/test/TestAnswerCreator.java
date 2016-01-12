package com.stevensadler.android.bloquery.test;

import com.stevensadler.android.bloquery.api.model.Answer;
import com.stevensadler.android.bloquery.api.model.Question;

/**
 * Created by Steven on 1/10/2016.
 */
public class TestAnswerCreator {

    public TestAnswerCreator() {}

    public void addAnswer(Question question, String body) {
        Answer testAnswer = new Answer();
        testAnswer.setQuestionId(question.getQuestionId());
        testAnswer.setBody(body);
        testAnswer.saveInBackground();
    }
}
