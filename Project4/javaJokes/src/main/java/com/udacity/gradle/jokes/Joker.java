package com.udacity.gradle.jokes;

import java.util.ArrayList;

public class Joker {

    private ArrayList<String> mList;
    private int i;

    public Joker() {
        mList = new ArrayList<>();
        mList.add("Yo daddy is so bald, when he wears a turtleneck he looks like a broken condom.");
        mList.add("Yo' Mama is so old, she was the only dinosaur they didn't have to animate for \"Jurassic Park.\"");
        i = 0;
    }

    public String getJoke() {
        if (mList.isEmpty()) {
            return "\"HA HA HA Joke's On You, Android boy\"";
        }

        i++;
        if (i > mList.size()-1) {
            i = 0;
        }

        return mList.get(i);
    }
}
