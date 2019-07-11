package com.vpapps.interfaces;

import com.vpapps.items.ItemFaq;

import java.util.ArrayList;

public interface FaqListenser {
    void onStart();
    void onEnd(String success,  ArrayList<ItemFaq> arrayListFaq);
}
