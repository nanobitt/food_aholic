package com.foodaholic.items;

import java.io.Serializable;

public class ItemMenu implements Serializable {

    private String id, name,type, image, desc, price,restId, catID, previous_price, rest_name;
    ItemRestaurant associatedRestaurant;

    public ItemMenu(String id, String name,String type, String image, String desc, String price, String previousPrice, String restId, String catID) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.price = price;
        this.restId = restId;
        this.catID = catID;
        this.type = type;
        this.previous_price = previousPrice;
    }

    //This constructor is used for food of the day
    public ItemMenu(String id, String name,String type, String image, String desc, String price, String restId, String catID, ItemRestaurant associatedRestaurant) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.price = price;
        this.restId = restId;
        this.catID = catID;
        this.type = type;
        this.associatedRestaurant = associatedRestaurant;
    }

    //Constructor is used for menu by search
    public ItemMenu(String id, String name,String type, String image, String price, String restId,  String rest_name) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.restId = restId;

        this.type = type;
        this.rest_name = rest_name;
    }

    public String getRest_name() {
        return rest_name;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getImage()
    {
        return image;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getPrice()
    {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public ItemRestaurant getAssociatedRestaurant() {
        return associatedRestaurant;
    }

    public void setAssociatedRestaurant(ItemRestaurant associatedRestaurant) {
        this.associatedRestaurant = associatedRestaurant;
    }

    public String getPrevious_price() {
        return previous_price;
    }
}
