package com.nanobit.interfaces;

import com.nanobit.items.ItemMenuCat;

import java.util.ArrayList;

public interface MenuCatListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenuCat> arrayList_menucat);
}
