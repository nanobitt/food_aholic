package com.nanobit.interfaces;

import com.nanobit.items.ItemMenu;

import java.util.ArrayList;

public interface FoodofthedayListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenu> arrayList_food_of_the_day);
}
