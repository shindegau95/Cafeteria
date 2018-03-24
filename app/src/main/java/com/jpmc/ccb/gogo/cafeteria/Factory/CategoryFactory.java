package com.jpmc.ccb.gogo.cafeteria.Factory;

import com.jpmc.ccb.gogo.cafeteria.FirestoreService;
import com.jpmc.ccb.gogo.cafeteria.Observer.CategoryObserver;
import com.jpmc.ccb.gogo.cafeteria.model.Category;
import com.jpmc.ccb.gogo.cafeteria.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Shinde on 02-03-2018.
 */

public class CategoryFactory implements CategoryObserver{

    private static CategoryFactory categoryFactory;
    private static final String TAG = "CategoryFactory";

    private static List<Category> categoryList;
    private static List<FoodItem> foodItemList;
    public static boolean noItems;

    private FirestoreService fs;

    public static CategoryFactory getInstance(){
        if(categoryFactory == null)
            categoryFactory = new CategoryFactory();

        return categoryFactory;
    }

    public static void resetInstance(){
        categoryFactory = null;
    }

    public CategoryFactory() {
        noItems = true;
        fs = new FirestoreService();
        fs.registerObserver(this);
        initialize();
    }

    private void initialize() {
        InitializeCategoryList();
        //InitializeFoodItems();
        //assignToCategory();
    }

    private void InitializeCategoryList() {
        if(categoryList == null)
            categoryList = new ArrayList<>();
    }
    /*public void InitializeFoodItems(){
        if(foodItemList == null ||  noItems) {
            foodItemList = new ArrayList<>();
            foodItemList.add(new FoodItem(1, "Dosa", 19, true));
            foodItemList.add(new FoodItem(2, "Pineapple juice", 25, true));
            foodItemList.add(new FoodItem(3, "Croissant", 25, true));
            foodItemList.add(new FoodItem(4, "Noodles", 25, true));
            foodItemList.add(new FoodItem(5, "Bhelpuri", 25, true));
            foodItemList.add(new FoodItem(6, "Pasta", 25, true));
            foodItemList.add(new FoodItem(7, "Tea", 25, true));
            foodItemList.add(new FoodItem(8, "Chicken Lollipop", 25, false));

        }
    }

    public void assignToCategory(){
        if(categoryList!=null && !categoryList.isEmpty()) {
            categoryList.get(0).addToCategory(getFoodItemList().get(0));
            categoryList.get(0).addToCategory(getFoodItemList().get(7));
        }
    }*/

    public Category getCategory(int categoryId){
        for(Category c: categoryList){
            if(c.getCategory_id() == categoryId)
                return  c;
        }
        return  null;
    }


    public void getCat(CategoryObserver categoryObserver){
        fs.registerObserver(categoryObserver);
        fs.refreshCategories(this.getCategoryList());
    }

    @Override
    public void dataChanged(ArrayList<Category> categories) {
        categoryList = categories;
        initialize();
    }
    //Getters and Setters

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public static List<FoodItem> getFoodItemList() {
        return foodItemList;
    }

    public static void setFoodItemList(List<FoodItem> foodItemList) {
        CategoryFactory.foodItemList = foodItemList;
    }


}
