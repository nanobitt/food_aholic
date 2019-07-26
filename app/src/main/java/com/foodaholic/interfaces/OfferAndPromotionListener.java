package com.foodaholic.interfaces;

import com.foodaholic.items.ItemOfferAndPromotion;

import java.util.ArrayList;

public interface OfferAndPromotionListener {
    void onStart();
    void onEnd(String success,  ArrayList<ItemOfferAndPromotion> arrayListOfferandPromotions);
}
