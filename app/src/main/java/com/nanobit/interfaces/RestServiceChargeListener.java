package com.nanobit.interfaces;

public interface RestServiceChargeListener {
    void onStart();
    void onEnd(String success, String resp);
}
