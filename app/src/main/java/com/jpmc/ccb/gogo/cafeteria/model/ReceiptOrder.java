package com.jpmc.ccb.gogo.cafeteria.model;

/**
 * Created by Gaurav Shinde on 01-03-2018.
 */

public class ReceiptOrder {

    private FoodItem foodItem;
    private int quantity;

    public ReceiptOrder(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
