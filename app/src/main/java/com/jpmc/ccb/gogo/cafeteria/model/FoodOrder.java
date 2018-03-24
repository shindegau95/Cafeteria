package com.jpmc.ccb.gogo.cafeteria.model;


/**
 * Created by Gaurav Shinde on 07-02-2018.
 */

public class FoodOrder {
    private FoodItem foodItem;
    private int quantity;

    public FoodOrder(FoodItem foodItem) {
        this.foodItem = foodItem;
        this.quantity = 0;
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

    public int incrementqty() {
        quantity++;
        return quantity;
    }

    public int decrementqty() {
        if(quantity > 0)
            quantity--;
        return quantity;
    }
}
