package com.vpapps.interfaces;

import com.vpapps.items.ItemCart;
import com.vpapps.items.ItemMenu;

import java.util.ArrayList;

public interface CartListener {
    void onStart();
    void onEnd(String success, String message, ArrayList<ItemCart> arrayList_menu);
}
