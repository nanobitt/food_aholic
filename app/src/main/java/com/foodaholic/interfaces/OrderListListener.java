package com.foodaholic.interfaces;

import com.foodaholic.items.ItemOrderList;

import java.util.ArrayList;

public interface OrderListListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemOrderList> arrayListOrderList);
}
