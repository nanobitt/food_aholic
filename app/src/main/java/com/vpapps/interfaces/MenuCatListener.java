package com.vpapps.interfaces;

import com.vpapps.items.ItemMenuCat;
import com.vpapps.items.ItemRestaurant;

import java.util.ArrayList;

public interface MenuCatListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenuCat> arrayList_menucat);
}
