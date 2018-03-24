package com.jpmc.ccb.gogo.cafeteria.Observer;

import com.jpmc.ccb.gogo.cafeteria.model.Category;

import java.util.ArrayList;

/**
 * Created by Gaurav Shinde on 26-02-2018.
 */

public interface CategoryObserver {
    void dataChanged(ArrayList<Category> categories);
}
