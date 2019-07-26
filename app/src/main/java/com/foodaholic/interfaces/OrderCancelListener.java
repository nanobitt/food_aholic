package com.foodaholic.interfaces;

public interface OrderCancelListener {
    void onStart();
    void onEnd(String success, String message);
}