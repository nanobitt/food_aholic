package com.vpapps.interfaces;

import com.vpapps.items.ItemMenu;
import com.vpapps.items.ItemMenuCat;

import java.util.ArrayList;

public interface MenuListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenu> arrayList_menu);
}
