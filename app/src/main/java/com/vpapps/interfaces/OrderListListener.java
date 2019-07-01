package com.vpapps.interfaces;

import com.vpapps.items.ItemMenuCat;
import com.vpapps.items.ItemOrderList;

import java.util.ArrayList;

public interface OrderListListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemOrderList> arrayListOrderList);
}
