package com.vpapps.interfaces;

import com.vpapps.items.ItemOfferAndPromotion;

import java.util.ArrayList;

public interface OfferAndPromotionListener {
    void onStart();
    void onEnd(String success,  ArrayList<ItemOfferAndPromotion> arrayListOfferandPromotions);
}
