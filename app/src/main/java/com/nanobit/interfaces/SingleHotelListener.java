package com.nanobit.interfaces;

import com.nanobit.items.ItemRestaurant;

public interface SingleHotelListener {
    void onStart();
    void onEnd(String success, ItemRestaurant itemRestaurant);
}
