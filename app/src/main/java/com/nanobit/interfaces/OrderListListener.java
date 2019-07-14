package com.nanobit.interfaces;

import com.nanobit.items.ItemOrderList;

import java.util.ArrayList;

public interface OrderListListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemOrderList> arrayListOrderList);
}
