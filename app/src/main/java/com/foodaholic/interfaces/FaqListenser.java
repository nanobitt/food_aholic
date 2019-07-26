package com.foodaholic.interfaces;

import com.foodaholic.items.ItemFaq;

import java.util.ArrayList;

public interface FaqListenser {
    void onStart();
    void onEnd(String success,  ArrayList<ItemFaq> arrayListFaq);
}
