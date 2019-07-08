package com.vpapps.items;

import java.io.Serializable;

public class ItemOfferAndPromotion implements Serializable {

    private String id, heading, description, image_link, post_date;

    public ItemOfferAndPromotion(String id, String heading, String description, String image_link, String post_date) {
        this.id = id;
        this.heading = heading;
        this.description = description;
        this.image_link = image_link;
        this.post_date = post_date;
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
