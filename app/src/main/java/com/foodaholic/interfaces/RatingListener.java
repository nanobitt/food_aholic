package com.foodaholic.interfaces;

public interface RatingListener {
    void onStart();
    void onEnd(String success, String message, float rating);
}
