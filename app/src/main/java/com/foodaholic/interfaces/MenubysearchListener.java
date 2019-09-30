package com.foodaholic.interfaces;

import com.foodaholic.items.ItemMenu;

import java.util.ArrayList;

public interface MenubysearchListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenu> menus);
}
