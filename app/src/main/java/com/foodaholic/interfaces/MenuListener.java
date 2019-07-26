package com.foodaholic.interfaces;

import com.foodaholic.items.ItemMenu;

import java.util.ArrayList;

public interface MenuListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenu> arrayList_menu);
}
