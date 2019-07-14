package com.nanobit.interfaces;

import com.nanobit.items.ItemCart;

import java.util.ArrayList;

public interface CartListener {
    void onStart();
    void onEnd(String success, String message, ArrayList<ItemCart> arrayList_menu);
}
