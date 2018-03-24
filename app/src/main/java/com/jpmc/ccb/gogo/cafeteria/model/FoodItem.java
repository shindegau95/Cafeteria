package com.jpmc.ccb.gogo.cafeteria.model;

/**
 * Created by DELL on 07-02-2018.
 */

public class FoodItem {
    private int foodItem_id;
    private String foodItem_name;
    private int price;
    private int thumbnail;
    private boolean Veg;

    public FoodItem() {
    }

    public FoodItem(int foodItem_id, String foodItem_name, int price, boolean Veg) {
        this.foodItem_id = foodItem_id;
        this.foodItem_name = foodItem_name;
        this.price = price;
        this.Veg = Veg;
    }

    public int getFoodItem_id() {
        return foodItem_id;
    }

    public void setFoodItem_id(int foodItem_id) {
        this.foodItem_id = foodItem_id;
    }

    public String getFoodItem_name() {
        return foodItem_name;
    }

    public void setFoodItem_name(String foodItem_name) {
        this.foodItem_name = foodItem_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isVeg() {
        return Veg;
    }

    public void setVeg(boolean veg) {
        Veg = veg;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "foodItem_id=" + foodItem_id +
                ", foodItem_name='" + foodItem_name + '\'' +
                ", price=" + price +
                ", thumbnail=" + thumbnail +
                ", isVeg=" + Veg +
                '}';
    }
}
