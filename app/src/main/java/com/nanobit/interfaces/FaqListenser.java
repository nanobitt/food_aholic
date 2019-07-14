package com.nanobit.interfaces;

import com.nanobit.items.ItemFaq;

import java.util.ArrayList;

public interface FaqListenser {
    void onStart();
    void onEnd(String success,  ArrayList<ItemFaq> arrayListFaq);
}
