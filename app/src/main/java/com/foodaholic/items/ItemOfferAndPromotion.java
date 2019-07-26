package com.foodaholic.items;

import java.io.Serializable;

public class ItemOfferAndPromotion implements Serializable {

    private String id, heading, description, image_link, post_date;
    ItemRestaurant associdatedRestaurant;

    public ItemOfferAndPromotion(String id, String heading, String description, String image_link, String post_date) {
        this.id = id;
        this.heading = heading;
        this.description = description;
        this.image_link = image_link;
        this.post_date = post_date;
        associdatedRestaurant = null;
    }

    public void setAssocidatedRestaurant(ItemRestaurant associdatedRestaurant) {
        this.associdatedRestaurant = associdatedRestaurant;
    }

    public boolean isBoundToARestaurant()
    {
        return associdatedRestaurant !=null;
    }

    public ItemRestaurant getAssocidatedRestaurant() {
        return associdatedRestaurant;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_link() {
        return image_link;
    }
}
