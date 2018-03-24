package com.jpmc.ccb.gogo.cafeteria.model;

import android.content.Context;

import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Shinde on 06-02-2018.
 */

public class Category {
    private String category_name;
    private int category_id;
    private int category_thumbnail;
    private List<FoodOrder> foodOrderList;

    public Category() {
    }

    public Category(int category_id, String category_name, String category_thumbnail_name){
        this.category_id = category_id;
        this.category_name = category_name;
        setCategory_thumbnail_by_categoryName(category_thumbnail_name);
        foodOrderList = new ArrayList<>();
    }

    public Category(int category_id, String category_name){
        this.category_id = category_id;
        this.category_name = category_name;
        setCategory_thumbnail_by_categoryName(category_name);
        foodOrderList = new ArrayList<>();
    }

    public int getCategory_thumbnail() {
        return category_thumbnail;
    }

    public void setCategory_thumbnail_by_categoryName(String category_name) {

        Context context = CafeteriaApplication.getInstance().getApplicationContext();
        this.category_thumbnail = context.getResources().getIdentifier("cover_"+category_name.toLowerCase().replace(" ",""), "drawable", context.getPackageName());
        if(this.category_thumbnail == 0){
            this.category_thumbnail = context.getResources().getIdentifier("cover_default", "drawable", context.getPackageName());
        }
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public List<FoodOrder> getFoodOrderList() {
        return foodOrderList;
    }

    public void setFoodOrderList(List<FoodOrder> foodOrderList) {
        this.foodOrderList = foodOrderList;
    }

    public void addToCategory(FoodItem m){
        //check if the menuitem is already there
        boolean menuItemAbsent = true;
        if(getFoodOrderList() == null)
            setFoodOrderList(new ArrayList<FoodOrder>());
        for(FoodOrder o : getFoodOrderList()){
            if(o.getFoodItem().getFoodItem_id() == m.getFoodItem_id()){
                menuItemAbsent = false;
            }
        }
        if(menuItemAbsent)
            foodOrderList.add(new FoodOrder(m));
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_name='" + category_name + '\'' +
                ", category_id=" + category_id +
                '}';
    }
}
