package com.foodaholic.interfaces;

import com.foodaholic.items.ItemPromo;

public interface PromoCodeListener {
    void onStart();
    void onEnd(String success, String result, String msg, ItemPromo promo);
}
