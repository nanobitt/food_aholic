package com.nanobit.interfaces;

import com.nanobit.items.ItemOfferAndPromotion;

import java.util.ArrayList;

public interface OfferAndPromotionListener {
    void onStart();
    void onEnd(String success,  ArrayList<ItemOfferAndPromotion> arrayListOfferandPromotions);
}
