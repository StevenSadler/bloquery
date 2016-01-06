package com.stevensadler.android.bloquery.api.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Steven on 1/3/2016.
 */
@ParseClassName("Question")
public class Question extends ParseObject {

    public String getBody() {
        return getString("body");
    }

    public void setBody(String value) {
        put("body", value);
    }
}
