package com.nanobit.interfaces;

import com.nanobit.items.ItemRestaurant;

import java.util.ArrayList;

public interface HomeListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemRestaurant> arrayList_latest, ArrayList<ItemRestaurant> arrayList_featured);
}
