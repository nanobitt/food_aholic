package com.foodaholic.interfaces;

import com.foodaholic.items.ItemRestaurant;

public interface SingleHotelListener {
    void onStart();
    void onEnd(String success, ItemRestaurant itemRestaurant);
}
